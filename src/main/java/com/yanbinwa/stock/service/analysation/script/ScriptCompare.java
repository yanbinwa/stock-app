package com.yanbinwa.stock.service.analysation.script;

/**
 * 比较的方法，大于，大于等于，小于，小于等于，还是等于
 * 
 * 每个方法可以列一个比较的方法
 * 
 * @author emotibot
 *
 */
public enum ScriptCompare
{
    EQ()
    {
        @Override
        public boolean getValue(double comp1, double comp2)
        {
            return comp1 == comp2;
        }
    },
    GT()
    {
        @Override
        public boolean getValue(double comp1, double comp2)
        {
            return comp1 > comp2;
        }
    },
    GTE()
    {
        @Override
        public boolean getValue(double comp1, double comp2)
        {
            return comp1 >= comp2;
        }
    },
    LT()
    {

        @Override
        public boolean getValue(double comp1, double comp2)
        {
            return comp1 < comp2;
        }
        
    },
    LTE()
    {

        @Override
        public boolean getValue(double comp1, double comp2)
        {
            return comp1 <= comp2;
        }
        
    };
    
    public abstract boolean getValue(double comp1, double comp2);
}
