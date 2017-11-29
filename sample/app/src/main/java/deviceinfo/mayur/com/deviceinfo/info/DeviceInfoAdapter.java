package deviceinfo.mayur.com.deviceinfo.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import deviceinfo.mayur.com.deviceinfo.R;
import deviceinfo.mayur.com.deviceinfo.model.SettingInfo;

/**
 * Created by mayurkaul on 04/10/17.
 */

public class DeviceInfoAdapter extends RecyclerView.Adapter<DeviceInfoViewHolder> {
    private ArrayList<SettingInfo>  mDeviceInfoList;
    private Context mContext;

    public  DeviceInfoAdapter(Context context, ArrayList<SettingInfo> infoList){
        mDeviceInfoList=infoList;
        mContext=context;
    }


    @Override
    public DeviceInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.device_info_item,parent,false);
        return new DeviceInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeviceInfoViewHolder holder, int position) {
        holder.bind(mDeviceInfoList.get(position),mContext);
    }

    @Override
    public int getItemCount() {
        return mDeviceInfoList!=null?mDeviceInfoList.size():0;
    }

}
