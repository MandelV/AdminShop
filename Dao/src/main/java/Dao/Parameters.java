package Dao;

/**
 *Represente Parameters which used by CallStatement
 * the paramters with stocked routines can be IN OUT or INOUT
 * it is a generic Class
 * @version 1.0
 */
public class Parameters<T> {
    /**
     * Represente Object Parameter
     * @see Object
     * */
    private T obj;


    /**
     * Contructor Parameters
     * @param obj Object of this parameter
     * @see Object
     * */
    public Parameters(final T obj){
        this.obj = obj;
    }

    /**
     * Set parameter object
     * @param obj Take any type (Integer, String)
     * @see Object
     * */
    public void setObj(T obj) {
        this.obj = obj;
    }


    /**
     * @return Object of this parameter
     * @see Object
     * */
    public T getParameter() {
        return obj;
    }


}
