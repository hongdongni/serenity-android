/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.browser.tv.seasons;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.RecyclerViewDiffUtil;

public class TVShowSeasonImageGalleryAdapter extends AbstractPosterImageGalleryAdapter {

  private List<SeriesContentInfo> seasonList = null;

  @Inject SerenityClient serenityClient;

  public TVShowSeasonImageGalleryAdapter() {
    super();
    seasonList = new ArrayList<>();
  }

  public Object getItem(int position) {
    if (seasonList.isEmpty()) {
      return null;
    }
    return seasonList.get(position);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_tvshow_indicator_view, null);
    SeasonViewHolder seasonViewHolder = new SeasonViewHolder(view);
    return seasonViewHolder;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    SeasonViewHolder viewHolder = (SeasonViewHolder) holder;

    SeriesContentInfo pi = seasonList.get(position);

    viewHolder.reset();
    viewHolder.createImage(pi, 120, 180);
    viewHolder.toggleWatchedIndicator(pi);
    viewHolder.getItemView().setOnClickListener((view)-> onItemViewClick(view, position));
    viewHolder.getItemView().setOnFocusChangeListener((view, focus)-> onItemViewFocusChanged(focus, view, position));
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemCount() {
    return seasonList.size();
  }

  public void updateSeasonsList(List<SeriesContentInfo> seasonList) {
    List<SeriesContentInfo> oldList = new ArrayList(this.seasonList);
    this.seasonList.clear();
    this.seasonList.addAll(seasonList);
    new RecyclerViewDiffUtil(oldList, this.seasonList).dispatchUpdatesTo(this);
  }

  public void onItemViewClick(View view, int i) {
    onItemClickListener = new TVShowSeasonOnItemClickListener(view.getContext(),this);
    onItemClickListener.onItemClick(view, i);
  }

  public void onItemViewFocusChanged(boolean hasFocus, View view, int i) {
    view.clearAnimation();
    view.setBackground(null);

    if (hasFocus && view != null) {
      view.clearAnimation();
      view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.rounded_transparent_border));
      zoomOut(view);
      getOnItemSelectedListener().onItemSelected(view, i);
      return;
    }

  }
}
