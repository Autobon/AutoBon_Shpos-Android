package cn.com.incardata.http.response;

/**
 * 发送验证码
 * Created by wanghao on 16/2/18.
 */
public class VerifySmsEntity extends BaseEntityTwo{
   private Object message;

   public Object getMessage() {
      return message;
   }

   public void setMessage(Object message) {
      this.message = message;
   }
}
