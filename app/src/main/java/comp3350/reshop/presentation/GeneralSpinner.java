package comp3350.reshop.presentation;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.ArrayRes;

public class GeneralSpinner {

	private final Context context;
	private final Spinner dropdown;
	private String currentOption;
	private final int arrayDropdownOptions;

	public GeneralSpinner(Context context, Spinner dropdown, @ArrayRes int arrayDropdownOptions) {
		this.context = context;
		this.dropdown = dropdown;
		currentOption = "";
		this.arrayDropdownOptions = arrayDropdownOptions;

		setUpDropdown();
	}

	private void setUpDropdown() {

		ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(
				context,
				arrayDropdownOptions,
				android.R.layout.simple_spinner_item
		);

		locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(locationAdapter);
		dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// Set the selected option
				final String defaultOption = context.getResources()
						.getStringArray(arrayDropdownOptions)[0];
				String selectedOption = parent.getItemAtPosition(position).toString();

				if (selectedOption.equals(defaultOption)) {
					currentOption = "";
				} else {
					currentOption = selectedOption;
				}
				Log.d("SpinnerSelector", "Selected option: " + currentOption);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.d("SpinnerSelector", "Nothing selected");
			}
		});
	}

	public String getSelectedOption() {
		return currentOption;
	}

	public void setSelectedOption(String option) {
		boolean set = false;
		for (int i = 0; i < dropdown.getCount() && !set; i++) {
			if (dropdown.getItemAtPosition(i).equals(option)) {
				dropdown.setSelection(i);
				set = true;
			}
		}
	}

	public void resetDropdown() {
		dropdown.setSelection(0);
	}
}
