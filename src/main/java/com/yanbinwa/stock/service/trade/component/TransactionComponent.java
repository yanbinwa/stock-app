package com.yanbinwa.stock.service.trade.component;

import com.yanbinwa.stock.service.trade.dao.AccountDao;
import com.yanbinwa.stock.service.trade.dao.StockAccountDao;
import com.yanbinwa.stock.service.trade.dao.StockTradeApplyDao;
import com.yanbinwa.stock.service.trade.dao.StockTradeDao;
import com.yanbinwa.stock.service.trade.element.StockTradeRequest;
import com.yanbinwa.stock.service.trade.entity.Account;
import com.yanbinwa.stock.service.trade.entity.StockAccount;
import com.yanbinwa.stock.service.trade.entity.StockTrade;
import com.yanbinwa.stock.service.trade.entity.StockTradeApply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@Transactional
@Slf4j
public class TransactionComponent {

    @Autowired
    StockTradeDao stockTradeDao;

    @Autowired
    StockTradeApplyDao stockTradeApplyDao;

    @Autowired
    StockAccountDao stockAccountDao;

    @Autowired
    AccountDao accountDao;

    @Transactional
    public boolean execStockTradeApply(StockTradeApply stockTradeApply) {
        StockTrade stockTrade = new StockTrade(stockTradeApply);
        try {
            stockTradeDao.save(stockTrade);
            stockTradeApplyDao.deleteById(stockTradeApply.getId());
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Transactional
    public boolean applyBuyStock(StockTradeRequest stockTradeRequest, Account account) {
        double buyAmount = stockTradeRequest.getNum() * stockTradeRequest.getPrice();
        double accountAmount = account.getDeposit();
        if (accountAmount < buyAmount) {
            log.info("账户余额不足");
            return false;
        }
        account.setDeposit(accountAmount - buyAmount);
        accountDao.save(account);
        StockTradeApply stockTradeApply = new StockTradeApply();
        stockTradeApply.setAccountId(stockTradeApply.getAccountId());
        stockTradeApply.setCreatedate(new Date());
        stockTradeApply.setNum(stockTradeRequest.getNum());
        stockTradeApply.setPrice(stockTradeRequest.getPrice());
        stockTradeApply.setStockId(stockTradeRequest.getStockId());
        stockTradeApply.setBuyStock(true);
        stockTradeApplyDao.save(stockTradeApply);
        return true;
    }

    @Transactional
    public boolean applySaleStock(StockTradeRequest stockTradeRequest) {
        StockAccount stockAccount = stockAccountDao.findOneByAccountIdAndStockId(stockTradeRequest.getAccountId(), stockTradeRequest.getStockId());
        if (stockAccount == null) {
            log.info("账户股票为0");
            return false;
        }
        int remainStockNum = stockAccount.getNum() - stockTradeRequest.getNum();
        if (remainStockNum < 0) {
            log.info("账户余额不足");
            return false;
        }

        if (remainStockNum == 0) {
            stockAccountDao.deleteById(stockAccount.getId());
        } else {
            stockAccount.setNum(remainStockNum);
            stockAccountDao.save(stockAccount);
        }
        StockTradeApply stockTradeApply = new StockTradeApply();
        stockTradeApply.setAccountId(stockTradeApply.getAccountId());
        stockTradeApply.setCreatedate(new Date());
        stockTradeApply.setNum(stockTradeRequest.getNum());
        stockTradeApply.setPrice(stockTradeRequest.getPrice());
        stockTradeApply.setStockId(stockTradeRequest.getStockId());
        stockTradeApply.setBuyStock(false);
        stockTradeApplyDao.save(stockTradeApply);
        return true;
    }

    /**
     * 退回账户金额
     *
     * @param stockTradeApply
     * @return
     */
    @Transactional
    public boolean recallBuyStockApply(StockTradeApply stockTradeApply) {
        double recallAmount = stockTradeApply.getPrice() * stockTradeApply.getPrice();
        Account account = accountDao.getOne(stockTradeApply.getAccountId());
        if (account == null) {
            log.info("找不到账户");
            return false;
        }
        account.setDeposit(account.getDeposit() + recallAmount);
        accountDao.save(account);
        stockTradeApplyDao.deleteById(stockTradeApply.getId());
        return true;
    }

    /**
     * 退回股票账户
     *
     * @param stockTradeApply
     * @return
     */
    @Transactional
    public boolean recallSaleStockApply(StockTradeApply stockTradeApply) {
        StockAccount stockAccount = stockAccountDao.findOneByAccountIdAndStockId(stockTradeApply.getAccountId(), stockTradeApply.getStockId());
        if (stockAccount == null) {
            stockAccount = new StockAccount();
            stockAccount.setAccountId(stockTradeApply.getAccountId());
            stockAccount.setStockId(stockTradeApply.getStockId());
            stockAccount.setNum(stockTradeApply.getNum());
        } else {
            stockAccount.setNum(stockAccount.getNum() + stockTradeApply.getNum());
        }
        stockAccountDao.save(stockAccount);
        stockTradeApplyDao.deleteById(stockTradeApply.getId());
        return true;
    }
}
