package cn.com.incardata.http.response;

/**评价
 * Created by wanghao on 16/4/26.
 */
public class CommentEntity extends BaseEntityTwo{
   private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
