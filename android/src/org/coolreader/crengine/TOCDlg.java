package org.coolreader.crengine;

import java.util.ArrayList;

import org.coolreader.CoolReader;
import org.coolreader.R;
import org.coolreader.crengine.adapter.TOCAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.onyx.android.sdk.ui.GridViewTOC;

public class TOCDlg extends BaseDialog {

    private CoolReader mCoolReader = null;
    private ReaderView mReaderView;
    private TOCItem mTOC;
    private GridViewTOC mGridViewTOC;
    private TOCItem mCurrentPageItem;
    private ArrayList<TOCItem> mItems = new ArrayList<TOCItem>();
    private LayoutInflater mInflater;
    private Button mButtonExit = null;
    private int mCurrentPage;

    private void initItems( TOCItem toc, boolean expanded )
    {
        for ( int i=0; i<toc.getChildCount(); i++ ) {
            TOCItem child = toc.getChild(i);
            if ( child.getPage()<=mCurrentPage ) {
                mCurrentPageItem = child;
            }
            if ( expanded ) {
                child.setGlobalIndex(mItems.size());
                mItems.add(child);
            } else {
                child.setGlobalIndex(-1); // invisible
            }
            initItems(child, expanded && child.getExpanded());
        }
    }
    private void initItems()
    {
        mCurrentPageItem = null;
        mItems.clear();
        initItems(mTOC, true);
    }

    private void expand( TOCItem item )
    {
        if ( item==null ) {
            return;
        }
        item.setExpanded(true);
        // expand all parents
        for ( TOCItem p = item.getParent(); p!=null; p = p.getParent() ) {
            p.setExpanded(true);
        }
        initItems();
        refreshList();
        if ( mItems.size()>0 ) {
            if ( item.getGlobalIndex()>=0 ) {
                mGridViewTOC.getGridView().setSelection(item.getGlobalIndex());
            } else {
                mGridViewTOC.getGridView().setSelection(0);
            }
        }
    }

    private void refreshList()
    {
        TOCAdapter adapter = new TOCAdapter(mInflater.getContext(), mGridViewTOC.getGridView(),
                mItems, mCurrentPageItem);
        mGridViewTOC.getGridView().setAdapter(adapter);
    }

    public TOCDlg( CoolReader coolReader, ReaderView readerView, TOCItem toc, int currentPage )
    {
        super(coolReader, coolReader.getResources().getString(R.string.win_title_toc), false, false);

        mCoolReader = coolReader;
        mReaderView = readerView;
        mTOC = toc;
        mCurrentPage = currentPage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(getContext());
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_toc);

        mButtonExit = (Button) findViewById(R.id.button_exit);
        mButtonExit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                TOCDlg.this.dismiss();
            }
        });

        mGridViewTOC = (GridViewTOC) findViewById(R.id.gridview_toc);
        mGridViewTOC.getGridView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listview, View view,
                    int position, long id) {
                TOCItem item = (TOCItem) view.getTag();
                if ( item.getChildCount()==0 || item.getExpanded() ) {
                    mReaderView.goToPage(item.getPage()+1);
                    dismiss();
                } else {
                    expand(item);
                }
            }
        });

        setFlingHandlers(mGridViewTOC, new Runnable() {
            @Override
            public void run() {
                TOCDlg.this.dismiss();
            }
        }, new Runnable() {
            @Override
            public void run() {
                TOCDlg.this.dismiss();
            }
        });

        expand( mTOC );
        expand( mCurrentPageItem );

        mCoolReader.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        TOCDlg.this.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    }
}
