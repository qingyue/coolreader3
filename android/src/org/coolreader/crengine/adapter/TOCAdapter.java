package org.coolreader.crengine.adapter;

import java.util.ArrayList;

import org.coolreader.crengine.TOCItem;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onyx.android.sdk.R;
import com.onyx.android.sdk.ui.OnyxGridView;
import com.onyx.android.sdk.ui.data.GridViewPageLayout.GridViewMode;
import com.onyx.android.sdk.ui.data.OnyxPagedAdapter;

public class TOCAdapter extends OnyxPagedAdapter
{
    private LayoutInflater mInflater = null;
    private ArrayList<TOCItem> mTOCItems = new ArrayList<TOCItem>();
    private TOCItem mTocItem = null;

    private static final int sItemMinWidth = 145;
    private static final int sItemMinHeight = 60;
    private static final int sHorizontalSpacing = 0;
    private static final int sVerticalSpacing = 0;
    private static final int sItemDetailMinHeight = 35;

    public TOCAdapter(Context context, OnyxGridView gridView, ArrayList<TOCItem> TOCItems, TOCItem tocItem)
    {
        super(gridView);

        mInflater = LayoutInflater.from(context);
        mTOCItems.addAll(TOCItems);
        mTocItem = tocItem;

        this.getPageLayout().setItemMinWidth(sItemMinWidth);
        this.getPageLayout().setItemMinHeight(sItemMinHeight);
        this.getPageLayout().setItemThumbnailMinHeight(sItemMinHeight);
        this.getPageLayout().setItemDetailMinHeight(sItemDetailMinHeight);
        this.getPageLayout().setHorizontalSpacing(sHorizontalSpacing);
        this.getPageLayout().setVerticalSpacing(sVerticalSpacing);
        this.getPageLayout().setViewMode(GridViewMode.Detail);

        this.getPaginator().initializePageData(mTOCItems.size(), this.getPaginator().getPageSize());
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View ret_view = null;
        if (convertView != null) {
            ret_view = convertView;
        } else {
            ret_view = mInflater.inflate(R.layout.gridview_toc_item, null);
        }

        int idx = this.getPaginator().getAbsoluteIndex(position);
        TOCItem toc_item = mTOCItems.get(idx);

        TextView title = (TextView) ret_view.findViewById(R.id.textview_title);
        TextView page = (TextView) ret_view.findViewById(R.id.textview_page);
        title.setSingleLine(true);
        page.setSingleLine(true);

        if (toc_item == mTocItem) {
            title.setTypeface(null, Typeface.BOLD);
            page.setTypeface(null, Typeface.BOLD);
        }
        else {
            title.setTypeface(null, Typeface.NORMAL);
            page.setTypeface(null, Typeface.NORMAL);
        }

        title.setText(toc_item.getName());
        page.setText(String.valueOf(toc_item.getPage() + 1));

        ret_view.setTag(toc_item);

        OnyxGridView.LayoutParams ret_view_params = new OnyxGridView.LayoutParams(this.getPageLayout().getItemCurrentWidth(),
                this.getPageLayout().getItemCurrentHeight());
        ret_view.setLayoutParams(ret_view_params);

        return ret_view;
    }
}
