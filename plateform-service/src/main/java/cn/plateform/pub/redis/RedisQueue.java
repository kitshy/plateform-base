package cn.plateform.pub.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class RedisQueue<T> extends AbstractQueue<T> {
    private RedisTemplate redisTemplate;
    private String queueName ;  //RedisQueue值
    private LinkedList<T> _list=new LinkedList<>();
    private boolean _inited=false;


    public void init(String qName,RedisTemplate rt)
    {
        queueName=qName;
        redisTemplate=rt;
        this._inited=true;
    }

    @Override
    public Iterator iterator(){
        return _list.iterator();
    }

    @Override
    public int size() {
        return redisTemplate.opsForList().size(queueName).intValue();
    }

    @Override
    public boolean offer(T o) {
        redisTemplate.opsForList().rightPush(queueName,o);
        return true;
    }

    public boolean push(T t)
    {
        return offer(t);
    }

    @Override
    public T poll() {
        if(size()>0) {
            T t = (T) redisTemplate.opsForList().leftPop(queueName);
            return t;
        }
        else{
            return null;
        }
    }

    public T pop()
    {
        return poll();
    }

    @Override
    public T peek() {
        if(size()>0) {
            T t = (T) redisTemplate.opsForList().index(queueName,0);/// .leftPop(queueName);
//            if(t!=null)
//                redisTemplate.opsForList().leftPush(queueName,t);
            return t;
        }
        else{
            return null;
        }
    }

    public List<T> toList()
    {
        if(this.size()>0) {
            return redisTemplate.opsForList().range(queueName, 0, this.size()-1);
        }
        else{
            return new ArrayList<>();
        }
    }


}
