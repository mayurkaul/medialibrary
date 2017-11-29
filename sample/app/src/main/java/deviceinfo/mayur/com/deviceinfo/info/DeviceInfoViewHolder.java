package deviceinfo.mayur.com.deviceinfo.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import deviceinfo.mayur.com.deviceinfo.R;
import deviceinfo.mayur.com.deviceinfo.model.SettingInfo;

/**
 * Created by mayurkaul on 04/10/17.
 */

public class DeviceInfoViewHolder extends RecyclerView.ViewHolder {

    private TextView mLabelText, mValueText;

    public DeviceInfoViewHolder(View itemView) {
        super(itemView);
        mLabelText=itemView.findViewById(R.id.label);
        mValueText=itemView.findViewById(R.id.value);
    }

    public void bind(SettingInfo info, Context context){
        mLabelText.setText(info.getLabel());
        mValueText.setText(info.getValue());
    }
}
