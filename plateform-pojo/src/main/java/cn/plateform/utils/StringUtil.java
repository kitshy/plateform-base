package cn.plateform.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {


    /**
     *将mlist平均截取targ个  list返回
     * @param targ
     * @param mList
     * @return
     */
    public static List<List<String>> splitList(int targ,List mList){
        List<List<String>>mEndList=new ArrayList<>();
        if( mList.size()%targ!=0) {
            for (int j = 0; j < mList.size() / targ + 1; j++) {
                if ((j * targ + targ) < mList.size()) {
                    mEndList.add(mList.subList(j * targ, j * targ + targ));//0-3,4-7,8-11    j=0,j+3=3   j=j*3+1
                } else if ((j * targ + targ) > mList.size()) {
                    mEndList.add(mList.subList(j * targ, mList.size()));
                } else if (mList.size() < targ) {
                    mEndList.add(mList.subList(0, mList.size()));
                }
            }
        }else if(mList.size()%targ==0){
            for (int j = 0; j < mList.size() / targ; j++) {
                if ((j * targ + targ) <= mList.size()) {
                    mEndList.add(mList.subList(j * targ, j * targ + targ));//0-3,4-7,8-11    j=0,j+3=3   j=j*3+1
                } else if ((j * targ+ targ) > mList.size()) {
                    mEndList.add(mList.subList(j * targ, mList.size()));
                } else if (mList.size() < targ) {
                    mEndList.add(mList.subList(0, mList.size()));
                }
            }
        }
        return mEndList;
    }


}
