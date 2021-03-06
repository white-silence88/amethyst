package core.response.transformers;
import core.response.adapters.ObjectIdTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {
    // Create instance GsonBuilder
    // Used for work with MongoDB uuid (not tranform to object)
    private Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}