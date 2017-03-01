package sample.data.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@RedisHash("sampleBean")
public class SampleBean {
    @Id
    String id;
    String value;
    Date date;

    public SampleBean(String value, Date date) {
        this.value = value;
        this.date = date;
    }
}
