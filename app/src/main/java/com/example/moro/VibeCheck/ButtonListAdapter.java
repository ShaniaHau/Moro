package com.example.moro.VibeCheck;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moro.EventBeskrivelseFragment;
import com.example.moro.R;

import java.util.ArrayList;
import java.util.List;


public class ButtonListAdapter extends RecyclerView.Adapter<ButtonListAdapter.MyViewHolder> {

    private Context myContext;
    private List<String> myButtons;

    public ButtonListAdapter(Context myContext, List<String> myButtons) {
        this.myContext = myContext;
        this.myButtons = myButtons;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater myInflater = LayoutInflater.from(myContext);
        view = myInflater.inflate(R.layout.gridview_item_buttons,parent,false);
        return new MyViewHolder(view);
    }

    ArrayList<TextView> choosenHvadButtonList = new ArrayList<>();
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.tv.setText(myButtons.get(position));
        //holder.tv.setPadding(40,40,40,40);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AppCompatActivity activity = (AppCompatActivity)view.getContext();
                //EventBeskrivelseFragment fragment = new EventBeskrivelseFragment();
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.event2All, fragment).addToBackStack(null).commit();

                if (choosenHvadButtonList.contains(holder.tv)){
                    holder.tv.setBackgroundResource(R.drawable.button_style_hvad);
                    holder.tv.setTextColor(Color.parseColor("#FFA500"));
                    choosenHvadButtonList.remove(holder.tv);
                }else if (!choosenHvadButtonList.contains(holder.tv)){
                    choosenHvadButtonList.add(holder.tv);
                    holder.tv.setBackgroundResource(R.drawable.button_style_hvad2);
                    holder.tv.setTextColor(Color.parseColor("#FFFDBA"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myButtons.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        LinearLayout layout;

        public MyViewHolder( @NonNull View itemView){
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.all_textview);
            layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }
    }
}