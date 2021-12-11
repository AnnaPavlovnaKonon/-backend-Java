import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "safeFlags",
    "highRiskFlags",
    "unsafeFlags",
    "wallUnsafeFlags",
    "showsAds"
})
@Generated("jsonschema2pojo")
public class AdConfig implements Serializable
{

    @JsonProperty("safeFlags")
    public List<String> safeFlags = null;
    @JsonProperty("highRiskFlags")
    public List<Object> highRiskFlags = null;
    @JsonProperty("unsafeFlags")
    public List<String> unsafeFlags = null;
    @JsonProperty("wallUnsafeFlags")
    public List<Object> wallUnsafeFlags = null;
    @JsonProperty("showsAds")
    public Boolean showsAds;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -6471089904226134849L;

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
