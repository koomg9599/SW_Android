package kr.ac.pusan.cs.bookforyou;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Adapter_Rcv extends RecyclerView.Adapter<Adapter_Rcv.ViewHolder> {

    private Activity activity;
    private ArrayList<Obj_book> dataList;
    //private ArrayList<Bitmap> imageList;
    public Adapter_Rcv(Activity activity, ArrayList<Obj_book> dataList) {
        this.activity =  activity;
        this.dataList = dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView author;
        TextView publicher;
        //TextView price;
        //TextView pubdate;

        public ViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title_text);
            author = (TextView) itemView.findViewById(R.id.author_text);
            publicher = (TextView) itemView.findViewById(R.id.company_text);
            //price = (TextView) itemView.findViewById(R.id.price);
            //pubdate = (TextView) itemView.findViewById(R.id.pubdate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(activity, "click " + dataList.get(getAdapterPosition()).title, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, Regist_Book_Info.class);
                    intent.putExtra("book",dataList.get(getAdapterPosition()));
                    //intent.putExtra("image",imageList.get(getAdapterPosition()));
                    activity.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(activity, "remove " + dataList.get(getAdapterPosition()).title, Toast.LENGTH_SHORT).show();
                    removeItem(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Obj_book tmpData = dataList.get(position);
        final Bitmap[] bitmap = new Bitmap[1];
        holder.title.setText(tmpData.title);
        holder.author.setText(tmpData.author);
        holder.publicher.setText(tmpData.publisher);
        //holder.price.setText(tmpData.price);
        //holder.pubdate.setText(tmpData.pubdate);

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try {
                    URL imageUrl = new URL(tmpData.image);
                    HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
            if(bitmap[0] !=null){

                holder.image.setImageBitmap(bitmap[0]);
                //imageList.add(bitmap[0]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size()); // 지워진 만큼 다시 채워넣기.
    }
}
