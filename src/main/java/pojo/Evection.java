package pojo;


import java.io.Serializable;
import java.util.Date;

/**
 * 出差申请中的流程变量对象
 * @author lyh
 * @date 2021/12/30
 */
public class Evection implements Serializable {
    /**
     * 主键ID
     */
    private Double num;

    /**
     * 出差单的名字
     */
    private String evectionName;

    /**
     * 出差天数
     */
    private Double days;

    /**
     * 出差开始时间
     */
    private Date beginDate;

    /**
     * 出差结束时间
     */
    private Date endDate;

    /**
     * 目的地
     */
    private String destination;

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public String getEvectionName() {
        return evectionName;
    }

    public void setEvectionName(String evectionName) {
        this.evectionName = evectionName;
    }

    public Double getDays() {
        return days;
    }

    public void setDays(Double days) {
        this.days = days;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
