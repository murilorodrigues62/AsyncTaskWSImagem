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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btAcessarWs;
    private ProgressBar mProgress;
    private java.net.URL url;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            url = new java.net.URL("http://cdn28.us1.fansshare.com/photograph/hdwallpapersmoviemobile/ned-stark-game-of-thrones-movie-mobile-wallpaper-174211401.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mProgress = (ProgressBar) findViewById(R.id.pb_carregando);
        btAcessarWs = (Button) findViewById(R.id.bt_acessar_ws);
        btAcessarWs.setOnClickListener(this);
    }
    public void onClick(View v) {
        if (v == btAcessarWs) {
            buscarImagem(url);
        }
    }
    private void buscarImagem(java.net.URL url){
        AsyncTask<java.net.URL, Void, Bitmap> tarefa = new AsyncTask<java.net.URL, Void, Bitmap>() {

            protected void onPreExecute() {
                super.onPreExecute();
                mProgress.setVisibility(View.VISIBLE);
            }
            protected Bitmap doInBackground(java.net.URL... params) {

                java.net.URL url = params[0];
                Bitmap imagem = null;

                try {
                     // O pacote org.apache.http foi substituido pelo java.net
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // HttpURLConnection é quem armazena a resposta da requisição enviada.
                    conn.setRequestMethod("POST");

                    // Se a resposta da requisição foi bem sucedida (HTTP 200)
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        // o fluxo de dados da resposta é bufferizado e convertido eu uma imagem
                        InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                        imagem = BitmapFactory.decodeStream(inputStream);
                    }

                } catch (IOException e) {
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