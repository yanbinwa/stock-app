package com.yanbinwa.stock.service.analysation.script;

/**
 * 用于collection中，限定collection中各个ScriptEle的关系
 * 
 * @author emotibot
 *
 */
public enum ScriptOp
{
    OR() {

        @Override
        public boolean getValue(boolean... compareValues)
        {
            boolean ret = true;
            for (int i = 0; i < compareValues.length; i ++)
            {
                if (!compareValues[i])
                {
                    ret = false;
                    break;
                }
            }
            return ret;
        }
        
    },
    
    AND() {

        @Override
        public boolean getValue(boolean... compareValues)
        {
            boolean ret = false;
            for (int i = 0; i < compareValues.length; i ++)
            {
                if (compareValues[i])
                {
                    ret = true;
                    break;
                }
            }
            return ret;
        }
        
    };
    public abstract boolean getValue(boolean... compareValues);
}
