package com.example.retrofittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HeaderMap;

public class MainActivity extends AppCompatActivity {
   private TextView textViewResult;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResult = findViewById(R.id.textViewResult);

        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header","xyz")
                                .build();
                        return chain.proceed(newRequest);

                    }
                })
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

       // getPosts();
       // getComments();
       // createPost();
        //deletePost
        updatePost();


    }
    public void getPosts(){

        Map<String,String> parameters = new HashMap<>();
        parameters.put("userId","1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");

       // Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,3,4},"id","desc");

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);


        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code :"+response.code());
                    return;
                }
                List<Post> posts = response.body();

                for(Post post : posts){
                    String content = "";
                    content += "ID"+post.getId()+"\n";
                    content +="User ID:"+post.getUserId()+"\n";
                    content +="Title:"+post.getTitle()+"\n";
                    content +="Text :"+post.getText()+"\n";
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

                textViewResult.setText(t.getMessage());

            }
        });
    }
    public void getComments(){
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments(2);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if(!response.isSuccessful()){
                    textViewResult.setText(response.code());
                    return;

                }
                List<Comment> listComments = response.body();

                for (Comment comment : listComments){
                    String content  ="";
                    content +="ID: "+comment.getId()+"\n";
                    content +="Post ID: "+comment.getPostId()+"\n";
                    content +="Name: "+comment.getName()+"\n";
                    content +="Mail: "+comment.getEmail()+"\n";
                    content +="Text: "+comment.getText()+"\n";
                    textViewResult.append(content);
                }


            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());

            }
        });
    }
    public void createPost(){
        //1-)Post post = new Post(21,"New Title","New Text");

         Map<String,String> fields = new HashMap<>();

        fields.put("userId:","23");
        fields.put("title","New Title");
        fields.put("text","New Text");
        //2-)Call<Post> call = jsonPlaceHolderApi.createPost(post);

       //2-) Call<Post> call = jsonPlaceHolderApi.createPost(23,"New Title","New Text");
        Call<Post> call = jsonPlaceHolderApi.createPost(fields);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText(response.code());
                    return;
                }
                Post postResponse = response.body();

                String content = "";
                content +="Code: "+response.code()+"\n";
                content +="ID: "+postResponse.getId()+"\n";
                content +="User ID: "+postResponse.getUserId()+"\n";
                content +="Tittle: "+postResponse.getTitle()+"\n";
                content +="Text: "+postResponse.getText()+"\n";

                textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());

            }
        });

    }
    public void updatePost(){
        Post post = new Post(12,null,"New Text");

        //putPost..Call<Post> call = jsonPlaceHolderApi.putPost("test..",5,post);

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("Map-Header1","def");
        headersMap.put("Map-Header2","abs");
        Call<Post> call = jsonPlaceHolderApi.patchPost(headersMap,5,post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful())
                    textViewResult.setText(response.code());
                Post postResponse = response.body();

                String content ="";
                content +="code: "+response.code()+"\n";
                content +="User ID: "+postResponse.getUserId()+"\n";
                content +="ID: "+postResponse.getId()+"\n";
                content +="Title: "+postResponse.getTitle()+"\n";
                content +="Text: "+postResponse.getText()+"\n";

                textViewResult.append(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }
    public void deletePost(){
        Post post = new Post(2,"New Title","new Text");
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                    textViewResult.setText(response.code());

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText(t.getMessage());

            }
        });
    }
}
