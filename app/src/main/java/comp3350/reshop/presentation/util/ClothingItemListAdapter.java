package comp3350.reshop.presentation.util;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import comp3350.reshop.R;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.presentation.PostActivity;

public class ClothingItemListAdapter extends RecyclerView.Adapter<ClothingItemListVH> {

	private List<ClothingItem> itemList;
	private final String currentUser;

	public ClothingItemListAdapter(List<ClothingItem> itemList, String currentUser) {
		this.itemList = itemList;
		this.currentUser = currentUser;
	}

	public void setSearchList(List<ClothingItem> itemSearchList) {
		this.itemList = itemSearchList;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public ClothingItemListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		 View view = LayoutInflater.from(parent.getContext())
				 .inflate(R.layout.clothing_item_list_item, parent, false);

		 return new ClothingItemListVH(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ClothingItemListVH holder, int position) {
		holder.setDetails(itemList.get(position),currentUser);
	}

	@Override
	public int getItemCount() {
		return itemList.size();
	}
}

class ClothingItemListVH extends RecyclerView.ViewHolder {
	private final TextView title, price;
	private final ImageView thumbnail;

	public ClothingItemListVH(@NonNull View itemView) {
		super(itemView);

		title = itemView.findViewById(R.id.clothingItemListCardTitle);
		price = itemView.findViewById(R.id.clothingItemListCardPrice);
		thumbnail = itemView.findViewById(R.id.clothingItemListImage);
	}

	public void setDetails(ClothingItem item,String currentUser) {
		title.setText(item.getItemName());
		price.setText(String.format(Locale.ROOT, "$%.2f", item.getPrice()/100.00));

		// Construct the URI for the image
		String uriString = item.getImageUriString();
		Uri uri = ImageViewerUtils.generateUriFromString(uriString);
		ImageViewerUtils.turnUriIntoDrawable(itemView.getContext(), thumbnail, uri, false);

		itemView.setOnClickListener(v -> {
			Intent intent = new Intent(v.getContext(), PostActivity.class);
			intent.putExtra("ITEM_ID", item.getItemID());
			intent.putExtra("CURRENT_USER", currentUser);
			v.getContext().startActivity(intent);
		});
	}
}

