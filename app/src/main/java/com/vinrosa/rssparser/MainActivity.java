package com.vinrosa.rssparser;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static class Feed {
        String name;
        String url;

        public Feed(String name, String url) {
            this.name = name;
            this.url = url;
        }
    }

    public static List<Feed> FEEDS = new ArrayList<>();
    static {
        FEEDS.add(new Feed("Diario Libre",
                "https://www.diariolibre.com/rss/portada.xml"));
        FEEDS.add(new Feed("Wired",
                "https://www.wired.com/feed/rss"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView feedSelectorListView = (ListView) findViewById(R.id.feedListView);
        feedSelectorListView.setAdapter(new FeedSelectorAdapter(FEEDS));
        feedSelectorListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Feed feed = (Feed) view.getTag();
        Intent intent = new Intent(this, FeedItemsActivity.class);
        intent.putExtra("URL", feed.url);
        intent.putExtra("TITLE", feed. name);
        startActivity(intent);
    }

    public class FeedSelectorAdapter extends ArrayAdapter<Feed> {
        public FeedSelectorAdapter(List<Feed> feeds){
            super(MainActivity.this, 0, feeds);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Feed feed = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.feed_selector_view, parent, false);
            }
            TextView name = convertView.findViewById(R.id.displayName);
            TextView url = convertView.findViewById(R.id.displayUrl);

            name.setText(feed.name);
            url.setText(feed.url);
            convertView.setTag(feed);

            return convertView;
        }
    }
}
