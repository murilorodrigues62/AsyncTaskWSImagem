package br.edu.ifspsaocarlos.sdm.asynctaskwsimagem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btAcessarWs;
    private ProgressBar mProgress;
    private final String url = "http://cdn28.us1.fansshare.com/photograph/hdwallpapersmoviemobile/ned-stark-game-of-thrones-movie-mobile-wallpaper-174211401.jpg";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgress = (ProgressBar) findViewById(R.id.pb_carregando);
        btAcessarWs = (Button) findViewById(R.id.bt_acessar_ws);
        btAcessarWs.setOnClickListener(this);
    }
    public void onClick(View v) {
        if (v == btAcessarWs) {
            buscarImagem(url);
        }
    }
    private void buscarImagem(String url){
        AsyncTask<String, Void, Bitmap> tarefa = new AsyncTask<String, Void, Bitmap>() {

            protected void onPreExecute() {
                super.onPreExecute();
                mProgress.setVisibility(View.VISIBLE);
            }
            protected Bitmap doInBackground(String... params) {

                /* Usando as classes do pacote org.apache.http é necessário criar um objeto
                   HttpGet (ou HttpPost) usando a URL para envio da requisição. */
                HttpGet httpGet = new HttpGet(params[0]);
                HttpClient httpClient = new DefaultHttpClient();
                Bitmap imagem = null;
                try {
                    // HttpResponse é quem armazena a resposta da requisição enviada.
                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    // Se a resposta da requisição foi bem sucedida (HTTP 200)
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                        // o fluxo de dados da resposta é bufferizado e convertido eu uma imagem
                        BufferedHttpEntity bufferedHttpEntity =
                                new BufferedHttpEntity(httpResponse.getEntity());
                        InputStream inputStream = bufferedHttpEntity.getContent();
                        imagem = BitmapFactory.decodeStream(inputStream);
                    }
                }
                catch (IOException e) {
                    Log.e((String) getText(R.string.app_name), getString(R.string.msg_erro));
                }
                return imagem;
            }
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                if (bitmap == null){
                    Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.msg_erro), Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    ImageView ivImagem = (ImageView) findViewById(R.id.iv_imagem);
                    ivImagem.setImageBitmap(bitmap);
                }
                mProgress.setVisibility(View.GONE);
            }
        };
        tarefa.execute(url);
    }
}