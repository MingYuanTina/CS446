package cs446.budgetMe.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.minidev.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection="transaction")
public class Transaction {

	@Id
    private String transId;
    private JSONObject category;
    private Double cost;
    private Date date;
    private String description;
    private String groupId;

    public Transaction() {}

    @JsonCreator
    public Transaction(@JsonProperty("category") JSONObject category,
                       @JsonProperty("cost") Double cost,
                       @JsonProperty("date") String date,
                       @JsonProperty("description") String description) throws Exception {
        this.category = category;
        this.cost = cost;
        SimpleDateFormat dateFormat =new SimpleDateFormat("dd/MM/yyyy");
        this.date = dateFormat.parse(date);
        this.description = description;
    }

    @JsonCreator
    public Transaction(@JsonProperty("transId") String transId,
                       @JsonProperty("category") JSONObject category,
                       @JsonProperty("cost") Double cost,
                       @JsonProperty("date") String date,
                       @JsonProperty("description") String description) throws Exception {
        this.transId = transId;
        this.category = category;
        this.cost = cost;
        SimpleDateFormat dateFormat =new SimpleDateFormat("dd/MM/yyyy");
        this.date = dateFormat.parse(date);
        this.description = description;
    }

    public String getTransId() { return this.transId; }

    public String getGroupId() { return this.groupId; }

    public JSONObject getCategory() { return this.category; }

    public Double getCost() { return this.cost; }

    public Date getDate() { return this.date; }

    public String getDescription() { return this.description; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public Boolean isEqual(Transaction trans1) {
        if (trans1.getCost().equals(this.cost) && trans1.getDate().equals(this.date) &&
            trans1.getDescription().equals(this.description) && trans1.getCategory().equals(this.category)) {
            return true;
        } else {
            return false;
        }
    }

}
