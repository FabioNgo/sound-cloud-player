package ngo.music.player.adapters;

import android.content.Context;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Controller.MenuController;
import ngo.music.player.Model.Category;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.CategoryManager;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.View.fragment.abstracts.ListItemsInCompositionListFragment;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.service.MusicPlayerService;

public abstract class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CompositionViewHolder>
		implements Constants.Models, Observer {
	private static final int NUM_ITEM_IN_ONE_CATEGORY = 5;
	public static class CompositionViewHolder extends RecyclerView.ViewHolder {

		// TODO Auto-generated constructor public class ViewHolder {
		public String objectId= "";
		public TextView[] items;
		public ImageView menu;
		public EditText editText;
		public ImageView editBtn;
		public ImageView submitBtn;
		public ImageView clearBtn;
		private int type = -1;
		public CompositionViewHolder(View v, final int type) {
			super(v);
			this.type = type;
			items = new TextView[NUM_ITEM_IN_ONE_CATEGORY + 1];
			items[0] = (TextView) v
					.findViewById(R.id.composition_view_cat);
			items[1] = (TextView) v.findViewById(R.id.item_1);
			items[2] = (TextView) v.findViewById(R.id.item_2);
			items[3] = (TextView) v.findViewById(R.id.item_3);
			items[4] = (TextView) v.findViewById(R.id.item_4);
			items[5] = (TextView) v.findViewById(R.id.item_5);
			menu = (ImageView)v.findViewById(R.id.composition_view_menu);
			editText = (EditText)v.findViewById(R.id.composition_view_cat_edit);
			editBtn = (ImageView)v.findViewById(R.id.composition_view_edit);
			submitBtn = (ImageView)v.findViewById(R.id.composition_view_submit);
			clearBtn = (ImageView)v.findViewById(R.id.composition_view_cancel);
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<Song> songs = ((CategoryManager)ModelManager.getInstance(type)).getSongsFromCategory(objectId);
					MusicPlayerService.getInstance().playNewSong(0,
							songs);
					return;
				}
			});
		}

	}

	protected ArrayList<Category> categories;
	Context context;
	int resource;
	private View v;
	private int type = -1;
	private boolean canDelete;
	private boolean canEdit;

	@Override
	public CompositionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = (LayoutInflater) parent.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.category_view, parent, false);
		CompositionViewHolder holder = new CompositionViewHolder(rootView,type);
		return holder;
	}

	@Override
	public void onBindViewHolder(final CompositionViewHolder holder, int position) {
		Category category = categories.get(position);
		ArrayList<Song> songs = getSongsFromCat(category.getId());
		holder.objectId = category.getId();
		/*
		 * Set song titles
		 */
		holder.items[0].setText(category.getTitle()); // playlist name
		/*
		 * Set song titles
		 */
		for (int i = 0; i < holder.items.length-1; i++) {
			String content = "" + (i + 1) + ". ";
			if(i<songs.size()) {
				content = content + songs.get(i).getName();
			}
			holder.items[i+1].setText(content);


		}

		/**
		 * Menu
		 */
		setMenuLayout(holder);
		/**
		 * Edit Text
		 */
		holder.editText.setVisibility(View.INVISIBLE);
		/**
		 * Clear Button
		 */
		holder.clearBtn.setVisibility(View.INVISIBLE);
		holder.clearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.items[0].setVisibility(View.VISIBLE);
				holder.editText.setVisibility(View.INVISIBLE);
				holder.editText.getLayoutParams().height = Helper.dpToPx(20, context);
				holder.editBtn.setVisibility(View.VISIBLE);
				holder.clearBtn.setVisibility(View.INVISIBLE);
				holder.submitBtn.setVisibility(View.INVISIBLE);
			}
		});
		/**
		 * submit Button
		 */
		setSubmitButton(holder);
	}

	public CategoryListAdapter() {
		super();
		type = setType();
		canDelete = setCanDelete();
		canEdit = setCanEdit();
		this.categories = new ArrayList<>();
		ArrayList<Model> temp = ModelManager.getInstance(type).getAll();
		for (Model model :temp) {
			if(model instanceof Category){
				categories.add((Category) model);
			}
		}
		ModelManager.getInstance(type).addObserver(this);

	}
	public ArrayList<Song> getSongs(int position){
		return ((CategoryManager)ModelManager.getInstance(type)).getSongsFromCategory(categories.get(position).getId());
	}
	public static CategoryListAdapter createNewInstance(int type) {
		// TODO Auto-generated method stub

		switch (type) {
		case PLAYLIST:
			return new PlaylistAdapter();
		case ALBUM:
			return new AlbumAdapter();
		case ARTIST:
			return new ArtistAdapter();

		default:
			return null;
		}

	}

	public static CategoryListAdapter getInstance(int type) {

		switch (type) {
		case PLAYLIST:
			if (PlaylistAdapter.instance == null) {
				createNewInstance(type);
			}
			return PlaylistAdapter.instance;
		case ALBUM:
			if (AlbumAdapter.instance == null) {
				createNewInstance(type);
			}
			return AlbumAdapter.instance;


		case ARTIST:
			if (ArtistAdapter.instance == null) {
				createNewInstance(type);
			}
			return ArtistAdapter.instance;

		default:
			return null;
		}
	}

	/**
	 *
	 * @return if the category can be editted or not
	 */
	protected abstract boolean setCanEdit();

	/**
	 * @return if the category item can be delete or not
	 */
	protected abstract boolean setCanDelete();

	/**
	 * @return type of Category in Constant.Category
	 */
	protected abstract int setType();


	/**
	 * get item from specific categories
	 * 
	 * @param id
	 *            : category
	 * @return list of songs
	 */
	protected ArrayList<Song> getSongsFromCat(String id) {
		return ((CategoryManager) ModelManager.getInstance(type)).getSongsFromCategory(id);

	}


	/**
	 * @param holder
	 */
	private void setSubmitButton(final CompositionViewHolder holder) {
		holder.submitBtn.setVisibility(View.INVISIBLE);
		holder.submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					((CategoryManager)ModelManager.getInstance(type)).updateTitle(holder.editText.getText().toString(),holder.objectId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					Helper.makeToastText(e.getMessage(), context);
					return;
				}
				holder.items[0].setVisibility(View.VISIBLE);
				holder.editText.getLayoutParams().height = Helper.dpToPx(20, context);
				holder.editText.setVisibility(View.VISIBLE);
				holder.editBtn.setVisibility(View.VISIBLE);
				holder.clearBtn.setVisibility(View.INVISIBLE);
				holder.submitBtn.setVisibility(View.INVISIBLE);
			}
		});
		if (canEdit) {
			holder.editBtn.setVisibility(View.VISIBLE);

		} else {
			holder.editBtn.setVisibility(View.GONE);
		}
		/**
		 * Edit Btn
		 */
		holder.editBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.editText.setVisibility(View.VISIBLE);
				holder.items[0].setVisibility(View.INVISIBLE);
				holder.editText.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
				
				holder.editText.setText(holder.items[0].getText());
				holder.editText.requestFocus();
				holder.editText.setSelection(holder.editText.getText().length());
				holder.clearBtn.setVisibility(View.VISIBLE);
				holder.submitBtn.setVisibility(View.VISIBLE);
				holder.editBtn.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * @param holder
	 */
	private void setMenuLayout(final CompositionViewHolder holder) {
		holder.menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(MusicPlayerMainActivity
						.getActivity(), holder.menu);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(
						R.menu.composition_list_item_menu, popup.getMenu());
				if (!canDelete) {
					popup.getMenu().findItem(R.id.composition_list_item_delete)
							.setVisible(false);
				}
				// registering popup with OnMenuItemClickListener
				Category category = (Category) ModelManager.getInstance(type).get(holder.objectId);
				ArrayList<Song> songs = ((CategoryManager) ModelManager.getInstance(type)).getSongsFromCategory(category.getId());

				popup.setOnMenuItemClickListener(MenuController.getInstance(songs,category));
				popup.show(); // showing popup menu
			}
		});
	}

	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof ModelManager) {
			this.categories.clear();
			ArrayList<Model> temp = ModelManager.getInstance(type).getAll();
			for (Model model :temp) {
				if(model instanceof Category){
					categories.add((Category) model);
				}
			}

			notifyDataSetChanged();
		}
	}


	public int getAdapterType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public int getItemCount() {
		return categories.size();
	}
}
