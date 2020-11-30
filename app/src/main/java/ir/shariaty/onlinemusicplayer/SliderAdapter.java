package ir.shariaty.onlinemusicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SliderAdapter extends  RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    private List<SliderItems> sliderItems;
    public SliderAdapter(List<SliderItems> sliderItems) {
        this.sliderItems = sliderItems;
    }
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            imageView.setImageResource(sliderItems.getImage());
        }
    }
}
