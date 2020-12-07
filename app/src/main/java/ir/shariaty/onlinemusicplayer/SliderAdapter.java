package ir.shariaty.onlinemusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SliderAdapter extends  RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    private List<SliderItems> sliderItems;
    Context context;

    public SliderAdapter(List<SliderItems> sliderItems) {
        this.sliderItems = sliderItems;
    }
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
            holder.setImage(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView imageView;

        public SliderViewHolder (@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.images_slide);
        }

        void setImage(SliderItems sliderItems) {
            Glide.with(context)
                    .load(sliderItems.getImage())
                    .override(300 , 300)
                    .into(imageView);
        }
    }
}
