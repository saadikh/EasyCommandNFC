package cnt.nfc.mbds.fr.easycommandnfc.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cnt.nfc.mbds.fr.easycommandnfc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private View view;
    private List<String> mDataset = new ArrayList<>();
    private RecyclerView mCategoryRecyclerView;
    private RecyclerView.Adapter mCategoryRecycleAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

//    private ArrayList<GoodsEntity> goodsEntityList = new ArrayList<GoodsEntity>();

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
//        initRecyclerView();
        initData();
        return view;
    }

    private void initData(){
        mDataset.add("Entrées");
        mDataset.add("Plats");
        mDataset.add("Dessert");
        mDataset.add("Vin");
        mDataset.add("Menu");

        initializeAdapter();
    }

    private void initializeAdapter() {
        mCategoryRecyclerView=view.findViewById(R.id.categoryList);
        mCategoryRecycleAdapter = new CategoryRecycleAdapter(getActivity(),mDataset);
        mCategoryRecyclerView.setAdapter(mCategoryRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
//        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCategoryRecyclerView.setLayoutManager(mLayoutManager);
        //设置item的分割线
//        mCategoryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
//        mCategoryRecycleAdapter.setOnItemClickListener(new CollectRecycleAdapter.OnItemClickListener() {
//            @Override
//            public void OnItemClick(View view, GoodsEntity data) {
//                //此处进行监听事件的业务处理
//                Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
//            }
//        });
    }


}
