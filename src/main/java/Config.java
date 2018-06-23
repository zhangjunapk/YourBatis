import org.zj.yourbatis.annotation.DataSource;
import org.zj.yourbatis.annotation.MapperScan;

/**
 * Created by ZhangJun on 2018/6/22.
 */
@MapperScan(value = {"org.zj.yourbatis.dao","org.zj.yourbatis.test"})
@DataSource(url = "jdbc:mysql://localhost:3306/yourbatis",username = "root",password = "",driver="com.mysql.jdbc.Driver")
public class Config {
}
