package sample.data.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


//@RedisHash("sampleBean")
@Document(indexName = "cva", type="sampleBean")
public class SampleBean {
    @Id
    String id;
    String value;
    LocalDate date;
    LocalDateTime dateTime;

    public SampleBean(String id, String value, LocalDate date, LocalDateTime dateTime) {
        this.id = id;
        this.value = value;
        this.date = date;
        this.dateTime = dateTime;
    }

    protected SampleBean(){}

    @Override
    public String toString() {
        return "SampleBean{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", date=" + date +
                ", dateTime=" + dateTime +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
