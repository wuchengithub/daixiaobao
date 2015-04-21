package com.daixiaobao.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.daixiaobao.R;
import com.daixiaobao.widget.ChangeMarkConformDialog.Builder;

public class ChangeMarkConformDialog extends Dialog {
	public ChangeMarkConformDialog(Context context, int theme) {
		super(context, theme);
	}

	public ChangeMarkConformDialog(Context context) {
		super(context);
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {

		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener,
				negativeButtonClickListener;
		private CompoundButton.OnCheckedChangeListener checkBoxOncheckedChangeListner;
		private String icon;
		private String checkBoxText;
		private String transformType;
		private EditText editTextMark;
		private TextView editTextOriPrice;
		
		public String getTransformType(){
			return transformType;
		}
		public Builder(Context context) {
			this.context = context;
		}
		public String getRemark() {
			// TODO Auto-generated method stub
			return editTextMark.getText().toString();
		}
		/**
		 * Set the Dialog message from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(String mark) {
			this.message = mark;
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}


		/**
		 * Set a custom content view for the Dialog. If a message is set, the
		 * contentView is not added to the Dialog...
		 * 
		 * @param v
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}


		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the positive button text and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Create the custom dialog
		 */
		public ChangeMarkConformDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final ChangeMarkConformDialog dialog = new ChangeMarkConformDialog(
					context, R.style.conformDialog);
			View layout = inflater.inflate(R.layout.change_mark_dialog,
					null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			// set the checkBox
			if (checkBoxText != null) {
				layout.findViewById(R.id.cb_dialog).setVisibility(View.VISIBLE);
				((CheckBox) layout.findViewById(R.id.cb_dialog))
						.setText(checkBoxText);
				if (checkBoxOncheckedChangeListner != null) {
					((CheckBox) layout.findViewById(R.id.cb_dialog))
							.setOnCheckedChangeListener(checkBoxOncheckedChangeListner);
				} else {
					// if no checkBox just set the visibility to GONE
					layout.findViewById(R.id.cb_dialog)
							.setVisibility(View.GONE);
				}
			}

			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			editTextMark = (EditText) layout.findViewById(R.id.remark_edittext);
			// set the content message
			if (message != null) {
				editTextMark.setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			// set the content icon
			if (icon != null) {
				((Button) layout.findViewById(R.id.Icon)).showContextMenu();
			}
			dialog.setContentView(layout);
			return dialog;
		}
	}
}
