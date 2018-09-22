package com.yanbinwa.stock.service.trade.utils;

import com.emotibot.middleware.utils.StringUtils;
import com.yanbinwa.stock.common.type.HourWindow;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import com.yanbinwa.stock.service.trade.component.TransactionComponent;
import com.yanbinwa.stock.service.trade.dao.AccountDao;
import com.yanbinwa.stock.service.trade.dao.StockAccountDao;
import com.yanbinwa.stock.service.trade.dao.StockTradeApplyDao;
import com.yanbinwa.stock.service.trade.dao.StockTradeDao;
import com.yanbinwa.stock.service.trade.element.BuildAccountRequest;
import com.yanbinwa.stock.service.trade.element.StockTradeRequest;
import com.yanbinwa.stock.service.trade.entity.Account;
import com.yanbinwa.stock.service.trade.entity.StockTrade;
import com.yanbinwa.stock.service.trade.entity.StockTradeApply;
import com.yanbinwa.stock.utils.HolidayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 这里会对交易进行处理
 * 
 * 1. 判读交易是否可以进行(当前的股价，是否在交易时间，当前账户余额是否足够，卖股票时账户里是否有足够的股票)
 * 2. 执行交易
 * 
 * 用户当日的买入交易都会单独存放，交易日结束后，进行结算，将这些股票合并到用户的股票账户中
 * 
 * 当日股票账户，股票账户，资金账户
 * 
 * 用户卖出股票时，先要查询股票账户，是否有足够的股票，如果卖出成功，直接将修改该用户的股票账户
 * 
 * 功能：
 * 
 * 1. 判断交易时间是否合法
 * 2. 判断股票是否停牌
 * 3. 买入判断资金会否足够
 * 4. 卖出股票是否足够
 * 5. 这里只是提交申请，之后有线程来追踪股票价格，如果满足，就可以进行交易
 * 6. 返回一个申请单号，可以查询
 * 7. 撤销股票申请(会将申请的资金回退到账户中)
 * 8. 查询申请
 *
 * 需要将service中的功能放在Utils中
 * 
 * @author emotibot
 *
 */
@Component
@EnableTransactionManagement
@Slf4j
public class StockTradeUtils
{
    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR9_SH, HourWindow.HOUR10_FH, HourWindow.HOUR10_SH, HourWindow.HOUR11_FH, HourWindow.HOUR13_FH, HourWindow.HOUR13_SH, HourWindow.HOUR14_FH, HourWindow.HOUR14_SH};
    public static StockTradeUtils stockTradeUtils;

    @Autowired
    AccountDao accountDao;
    
    @Autowired
    StockAccountDao stockAccountDao;

    @Autowired
    StockTradeApplyDao stockTradeApplyDao;

    @Autowired
    StockTradeDao stockTradeDao;

    @Autowired
    TransactionComponent transactionComponent;

    @PostConstruct
    public void init()
    {
        stockTradeUtils = this;
    }

    /**
     * 去除节假日，去除周末，9:30 - 11:30, 13:00 - 15:00
     * 
     * @param timestamp
     * @return
     */
    public static boolean isDealTime(long timestamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            return false;
        }
        if (HolidayUtils.isHoliday(new Date(timestamp)))
        {
            return false;
        }
        for (HourWindow hourWindow : hourWindowArray)
        {
            if (!HourWindow.isDateInWindow(calendar, hourWindow))
            {
                return false;
            }
        }
        return false;
    }
    
    /**
     * 股票是否停牌
     * 
     * @param stockId
     * @return
     */
    public static boolean isStockSuspension(String stockId)
    {
        StockTrend stockTrend = CollectionUtils.getStockTrendData(stockId);
        if (stockTrend == null || stockTrend.getHigh() == 0d) {
            return true;
        }
        return false;
    }
    
    /**
     * 查询账户余额
     * 
     * @param accountId
     * @return
     */
    public static double getAccountBalance(long accountId)
    {
        Account account = stockTradeUtils.accountDao.getOne(accountId);
        return account.getDeposit();
    }
    
//    /**
//     * 查询某一个账户的某一个股票持有信息
//     *
//     * @param accountId
//     * @param stockId
//     * @return
//     */
//    public static StockAccount getAccountStock(long accountId, String stockId)
//    {
//        Specification<StockAccount> querySpecifi = new Specification<StockAccount>()
//        {
//            @Override
//            public Predicate toPredicate(Root<StockAccount> root, CriteriaQuery<?> paramCriteriaQuery,
//                    CriteriaBuilder cb)
//            {
//                List<Predicate> predicates = new ArrayList<>();
//                predicates.add(cb.equal(root.get("accountId").as(Long.class), accountId));
//                predicates.add(cb.equal(root.get("stockId").as(String.class), stockId));
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        };
//        return stockTradeUtils.stockAccountDao.findOne(querySpecifi);
//    }

    public static Account getAccountByAccountId(long accountId) {
        return stockTradeUtils.accountDao.getOne(accountId);
    }

    public static void updateAccount(Account account) {
        stockTradeUtils.accountDao.save(account);
    }

    public static List<StockTrade> getStockTradeByAccountId(long accountId) {
        return stockTradeUtils.stockTradeDao.findAllByAccountId(accountId);
    }

    public static List<StockTradeApply> getAllStockTradeApply() {
        return stockTradeUtils.stockTradeApplyDao.findAll();
    }

    public static List<StockTradeApply> getStockTradeApplyByAccountId(long accountId) {
        return stockTradeUtils.stockTradeApplyDao.findAllByAccountId(accountId);
    }

    /**
     * 执行买卖申请，事务操作
     *
     */
    public static boolean execStockTradeApply(StockTradeApply stockTradeApply) {
        return stockTradeUtils.transactionComponent.execStockTradeApply(stockTradeApply);
    }

    public static boolean applyTradeStock(StockTradeRequest stockTradeRequest) {
        Account account = stockTradeUtils.getAccountByAccountId(stockTradeRequest.getAccountId());
        if (account == null) {
            return false;
        }
        if (stockTradeUtils.isStockSuspension(stockTradeRequest.getStockId())) {
            return false;
        }

        if (stockTradeRequest.isBuyStock()) {
            return stockTradeUtils.transactionComponent.applyBuyStock(stockTradeRequest, account);
        } else {
            return stockTradeUtils.transactionComponent.applySaleStock(stockTradeRequest);
        }
    }

    public static boolean recallTradeStock(Long id) {
        StockTradeApply stockTradeApply = stockTradeUtils.stockTradeApplyDao.getOne(id);
        if (stockTradeApply == null) {
            log.info("没有找到申请");
            return false;
        }
        if (stockTradeApply.isBuyStock()) {
            return stockTradeUtils.transactionComponent.recallBuyStockApply(stockTradeApply);
        } else {
            return stockTradeUtils.transactionComponent.recallSaleStockApply(stockTradeApply);
        }
    }

    public static boolean buildAccount(BuildAccountRequest buildAccountRequest) {
        if (StringUtils.isEmpty(buildAccountRequest.getName())) {
            return false;
        }
        Account account = stockTradeUtils.accountDao.findOneByName(buildAccountRequest.getName());
        if (account != null) {
            return false;
        }
        account = new Account();
        account.setName(buildAccountRequest.getName());
        account.setDeposit(buildAccountRequest.getDeposit());
        stockTradeUtils.accountDao.save(account);
        return true;
    }
}
