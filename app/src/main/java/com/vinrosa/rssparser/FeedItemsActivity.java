package com.vinrosa.rssparser;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

public class FeedItemsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_items);
        getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));
        String url = getIntent().getStringExtra("URL");
        final ListView feedItemsListView =
                (ListView) findViewById(R.id.feedItemsListView);

        feedItemsListView.setOnItemClickListener(this);
        Parser parser = new Parser();
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                feedItemsListView.setAdapter(new FeedItemsAdapter(list));
            }
            @Override
            public void onError() { }
        });
        parser.execute(url);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Article article = (Article) view.getTag();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Por favor seleccione el navegador")
                .setPositiveButton("Externo",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(article.getLink()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Interno",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(FeedItemsActivity.this, WebViewActivity.class);
                        intent.putExtra("URL", article.getLink());
                        startActivity(intent);
                    }
                }).create();
        dialog.show();
    }

    private class FeedItemsAdapter extends ArrayAdapter<Article>{
        public FeedItemsAdapter(ArrayList<Article> list) {
            super(FeedItemsActivity.this, 0, list);
        }

        @NonNull
        @Override
        public View getView(int position,
                            @Nullable View convertView,
                            @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.feed_item_view, parent, false);
            }

            Article article = getItem(position);
            TextView title = convertView.findViewById(R.id.feedTitle);
            TextView desc = convertView.findViewById(R.id.feedDesc);
            TextView date = convertView.findViewById(R.id.feedDate);

            title.setText(article.getTitle());
            desc.setText(article.getDescription());
            if (article.getPubDate() != null) {
                date.setText(article.getPubDate().toString());
            }
            convertView.setTag(article);
            return convertView;
        }
    }
}
