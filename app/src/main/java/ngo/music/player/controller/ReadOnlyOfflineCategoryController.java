package ngo.music.player.controller;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.player.entity.Category;

public abstract class ReadOnlyOfflineCategoryController extends
		OfflineCategoryController {

	@Override
	public void storeCategories() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Category createCategory(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void getCategoriesPostExcecute(ArrayList<Category> categories) {
		// TODO Auto-generated method stub
		
	}
}
