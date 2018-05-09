package mylayout.dyj.com.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanyuanjin
 * on 2018/5/8.
 */
public class FlowLayout extends ViewGroup {

    private List<Integer> mLineHeight = new ArrayList<>();
    private List<List<View>> mViews = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int widthSum = 0;
        int height = 0;
        int flowLayoutWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int flowLayoutHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int flowLayoutWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int flowLayoutHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.d("dyj", "flowLayoutWidthSize:" + flowLayoutWidthSize + ",flowLayoutHeightSize:" + flowLayoutHeightSize);
        List<View> views = new ArrayList<>();
        mViews.clear();
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            widthSum = widthSum + childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            height = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            if (widthSum > flowLayoutWidthSize) {
                mViews.add(views);
                views = new ArrayList<>();
                views.add(childView);
                mLineHeight.add(childView.getMeasuredHeight());
                height += childView.getMeasuredHeight();
                widthSum = 0;
            } else {
                views.add(childView);
            }
            if (i == getChildCount() - 1) {
                mViews.add(views);
                mLineHeight.add(childView.getMeasuredHeight());
                height += childView.getMeasuredHeight();
            }
        }
        Log.d("dyj", "widthSum:" + widthSum + ",height:" + height);
        setMeasuredDimension(flowLayoutWidthSize, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int widthSum = 0;
        int heightSum = 0;
        Log.d("dyj", "mList.size():" + mViews.size());
        for (int i = 0; i < mViews.size(); i++) {
            int heightLine = mLineHeight.get(i);
            Log.d("dyj", "mLists.get(i).size():" + mViews.get(i).size());
            for (int j = 0; j < mViews.get(i).size(); j++) {
                View childView = mViews.get(i).get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                childView.layout(widthSum + layoutParams.leftMargin, heightSum + layoutParams.topMargin,
                        widthSum + childView.getMeasuredWidth() + layoutParams.rightMargin,
                        heightSum + childView.getMeasuredHeight() + layoutParams.bottomMargin);
                widthSum += childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            widthSum = 0;
            heightSum += heightLine;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
