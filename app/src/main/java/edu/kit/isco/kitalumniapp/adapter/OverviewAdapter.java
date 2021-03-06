package edu.kit.isco.kitalumniapp.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.kit.isco.kitalumniapp.OverviewListItem;
import edu.kit.isco.kitalumniapp.R;
import edu.kit.isco.kitalumniapp.dbObjects.DataAccessEvent;
import edu.kit.isco.kitalumniapp.dbObjects.DataAccessJob;
import edu.kit.isco.kitalumniapp.dbObjects.DataAccessNews;
import edu.kit.isco.kitalumniapp.dbServices.DBHandlerClient;

/**
 * Created by Andre on 22.02.2015.
 * An adapter class to get the objects shown in OverViewFragment
 */
public class OverviewAdapter extends ArrayAdapter {

    //a list of all objects shown in Overview
    private ArrayList<OverviewListItem> objects = new ArrayList<OverviewListItem>();

    //static ids to distinguish the list objects
    private static final int TYPE_NEWS = 0;
    private static final int TYPE_JOBS = 1;
    private static final int TYPE_EVENTS = 2;
    private static final int TYPE_HEADER = 3;

    private LayoutInflater mInflater;
    Future<List<DataAccessNews>> loadNews;
    Future<List<DataAccessJob>> loadJobs;
    Future<List<DataAccessEvent>> loadEvents;
    private int resource;
    private Context context;
    private final String SERVICE_URL;
    private ArrayList<DataAccessNews> newsFromDB;
    private ArrayList<DataAccessEvent> eventsFromDB;
    private ArrayList<DataAccessJob> jobsFromDB;

    /**
     * Constructor of OverviewAdapter
     * @param context
     * @param resource
     */
    public OverviewAdapter (Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SERVICE_URL = context.getResources().getString(R.string.rest_service_base_url);
        DBHandlerClient dbHandler = new DBHandlerClient(context);
        newsFromDB = (ArrayList<DataAccessNews>) dbHandler.getXnews(3);
        eventsFromDB = (ArrayList<DataAccessEvent>) dbHandler.getXevents(1);
        jobsFromDB = (ArrayList<DataAccessJob>) dbHandler.getXjobs(3);
    }

    /**
     * returns the object id of the object in a certain position of the list,
     * to determine the view to be used
     * @param position the position of the object in the list
     * @return object id
     */
    public int getItemViewType(int position) {
        int type = -1;
        if(objects.get(position).getId() == 0 ||
                objects.get(position).getId() == 1 ||
                objects.get(position).getId() == 2 ||
                objects.get(position).getId() == 3) {
            type = objects.get(position).getId();
        }
        return type;
    }

    /**
     * adds a new item to the list of Overview Objects
     * @param item the OverviewListItem to be added
     */
    public void addItem(OverviewListItem item) {
        if(item.getObject().getClass()== DataAccessNews.class ||
                item.getObject().getClass() == DataAccessJob.class ||
                item.getObject().getClass() == DataAccessEvent.class ||
                item.getObject().getClass() == String.class) {

            objects.add(item);
        } else {
            throw new IllegalArgumentException("The Object of OverviewListItem must be " +
                    "DataAccessNews, DataAccessJob, DataAccessEvent or String");
        }
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position).getObject();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    /**
     * Method to get the objects shown in overview either from local database or the server database
     */
    public void update() {
        if(newsFromDB.size() == 0) {
            loadLatestNews();
        } else {
            addItem(new OverviewListItem("Latest News", TYPE_HEADER));
            for (DataAccessNews n : newsFromDB) {
                addItem(new OverviewListItem(n, TYPE_NEWS));
            }
        }

        if(eventsFromDB.size() == 0) {
            loadLatestEvents();
        } else {
            addItem(new OverviewListItem("Next Event", TYPE_HEADER));
            addItem(new OverviewListItem(eventsFromDB.get(0), TYPE_EVENTS));
        }

        if(jobsFromDB.size() == 0) {
            loadLatestJobs();
        } else {
            addItem(new OverviewListItem("Latest Jobs", TYPE_HEADER));
            for (DataAccessJob i : jobsFromDB)
            addItem(new OverviewListItem(i, TYPE_JOBS));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        int rowType = getItemViewType(position);

        switch (rowType) {
            case TYPE_NEWS:
                if(convertView == null) {
                    convertView = mInflater.inflate(R.layout.list_view_item_news, null);
                    holder.image = (ImageView) convertView.findViewById(R.id.newsImage);
                    holder.textview1 = (TextView) convertView.findViewById(R.id.newsCaption);
                    holder.textview2 = (TextView) convertView.findViewById(R.id.newsShortDescription);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                DataAccessNews news = (DataAccessNews) getItem(position);
                Ion.with(holder.image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.default_news_image)
                        .load(news.getImageUrl());
                holder.textview1.setText(news.getTitle());
                holder.textview2.setText(news.getShortDescription());
                break;

            case TYPE_EVENTS:
                if(convertView == null) {
                    convertView = mInflater.inflate(R.layout.list_view_item_events, null);
                    holder.image = null;
                    holder.textview1 = (TextView) convertView.findViewById(R.id.eventTitle);
                    holder.textview2 = (TextView) convertView.findViewById(R.id.eventDate);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                DataAccessEvent event = (DataAccessEvent) getItem(position);
                holder.textview1.setText(event.getTitle());
                try {
                    holder.textview2.setText(DateFormat.format("dd.MM.yyyy", Long.parseLong(event.getDate())).toString());
                } catch (NumberFormatException e) {
                    holder.textview2.setText("");
                }
                break;

            case TYPE_JOBS:
                if(convertView == null) {
                    convertView = mInflater.inflate(R.layout.list_view_item_jobs, null);
                    holder.image = null;
                    holder.textview1 = (TextView) convertView.findViewById(R.id.jobsCaption);
                    holder.textview2 = (TextView) convertView.findViewById(R.id.jobsShortDescription);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                DataAccessJob job = (DataAccessJob) getItem(position);
                holder.textview1.setText(job.getTitle());
                holder.textview2.setText(job.getShortInfo());
                break;

            case TYPE_HEADER:
                if(convertView == null) {
                    convertView = mInflater.inflate(R.layout.overview_header, null);
                    holder.image = null;
                    holder.textview1 = (TextView) convertView.findViewById(R.id.section_header);
                    holder.textview2 = null;
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.textview1.setText((String) objects.get(position).getObject());
                break;

            default:
                break;
        }

        return convertView;
    }

    private void loadLatestNews() {
        // don't attempt to load more if a load is already in progress
        if (loadNews != null && !loadNews.isDone() && !loadNews.isCancelled()) {
            return;
        }
        /**
         * Ion is a general purpose networking library,
         * that's a successor to UrlImageViewHelper.
         * This request loads a URL as JsonArray
         * and invokes a callback on completion.
         */
        loadNews = Ion.with(getContext())
                .load(SERVICE_URL + "news/latest/")
                .as(new TypeToken<List<DataAccessNews>>() {
                })
                .setCallback(new FutureCallback<List<DataAccessNews>>() {
                    @Override
                    public void onCompleted(Exception e, List<DataAccessNews> result) {
                        // this is called back onto the ui thread, no Activity.runOnUiThread or Handler.post necessary.
                        if (e != null) {
                            Toast.makeText(getContext(), "Error loading news.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        addItem(new OverviewListItem("Latest News", TYPE_HEADER));
                        if (result != null) {
                            // add the news
                            final ArrayList<DataAccessNews> n = new ArrayList<DataAccessNews>();
                            Collections.reverse(result);
                            for (int i = 0; i < result.size(); i++) {
                                n.add(result.get(i));
                            }
                            for (int j = 0; j < 3 && j < result.size(); j++) {
                                addItem(new OverviewListItem(result.get(j), TYPE_NEWS));

                            }
                            notifyDataSetChanged();

                            new Runnable() {
                                @Override
                                public void run() {
                                    new DBHandlerClient(context).updateNews(n);
                                }
                            }.run();
                        }

                    }
                });
    }

    private void loadLatestJobs() {
        // don't attempt to load more if a load is already in progress
        if (loadJobs != null && !loadJobs.isDone() && !loadJobs.isCancelled()) {
            return;
        }
        /**
         * Ion is a general purpose networking library,
         * that's a successor to UrlImageViewHelper.
         * This request loads a URL as JsonArray
         * and invokes a callback on completion.
         */
        loadJobs = Ion.with(getContext())
                .load(SERVICE_URL + "jobs/latest/")
                .as(new TypeToken<List<DataAccessJob>>() {
                })
                .setCallback(new FutureCallback<List<DataAccessJob>>() {
                    @Override
                    public void onCompleted(Exception e, List<DataAccessJob> result) {
                        // this is called back onto the ui thread, no Activity.runOnUiThread or Handler.post necessary.
                        if (e != null) {
                            Toast.makeText(getContext(), "Error loading jobs.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        addItem(new OverviewListItem("Newest Jobs", TYPE_HEADER));
                        if (result != null) {
                            Collections.reverse(result);
                            final ArrayList<DataAccessJob> j = new ArrayList<DataAccessJob>();
                            // add the jobs
                            for (int i = 0; i < result.size(); i++) {
                                if (i < 3) {
                                    addItem(new OverviewListItem(result.get(i), TYPE_JOBS));
                                }
                                j.add(result.get(i));
                            }
                            notifyDataSetChanged();

                            new Runnable() {
                                @Override
                                public void run() {
                                    new DBHandlerClient(context).updateJobs(j);
                                }
                            }.run();
                        }
                    }
                });
    }

    private void loadLatestEvents() {
        // don't attempt to load more if a load is already in progress
        if (loadEvents != null && !loadEvents.isDone() && !loadEvents.isCancelled()) {
            return;
        }
        /**
         * Ion is a general purpose networking library,
         * that's a successor to UrlImageViewHelper.
         * This request loads a URL as JsonArray
         * and invokes a callback on completion.
         */
        loadEvents = Ion.with(getContext())
                .load(SERVICE_URL + "events/")
                .as(new TypeToken<List<DataAccessEvent>>() {
                })
                .setCallback(new FutureCallback<List<DataAccessEvent>>() {
                    @Override
                    public void onCompleted(Exception e, List<DataAccessEvent> result) {
                        // this is called back onto the ui thread, no Activity.runOnUiThread or Handler.post necessary.
                        if (e != null) {
                            Toast.makeText(getContext(), "Error loading events.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (result == null) {
                            return;
                        }

                        // add the event
                        Collections.reverse(result);
                        addItem(new OverviewListItem("Next Event", TYPE_HEADER));
                        final ArrayList<DataAccessEvent> ev = new ArrayList<DataAccessEvent>();
                        if (result != null) {
                            addItem(new OverviewListItem(result.get(0), TYPE_EVENTS));
                            for (int i = 0; i <result.size(); i++) {
                                ev.add(result.get(i));
                            }
                        }

                        notifyDataSetChanged();

                        new Runnable() {
                            @Override
                            public void run() {
                                new DBHandlerClient(context).updateEvents(ev);
                            }
                        }.run();
                    }
                });
    }

    /**
     * Container class for the objects to be viewed
     */
    static class ViewHolder {
        TextView textview1;
        TextView textview2;
        ImageView image;
    }
}

