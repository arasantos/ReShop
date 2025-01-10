package comp3350.reshop;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;

import static org.hamcrest.CoreMatchers.is;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

public class RecyclerViewItemCountAssertion implements ViewAssertion {
    private final int expectedCount;

    public RecyclerViewItemCountAssertion(int count) {
        this.expectedCount = count;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), is(expectedCount));
    }
}