package com.example.sitesyncversionbeta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.Employee

class EmployeeAdapter(
    context: Context,
    private val employees: List<Employee>
) : ArrayAdapter<Employee>(context, 0, employees) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_employee_list, parent, false)
        }

        val employee = employees[position]

        val nameTextView: TextView = itemView!!.findViewById(R.id.nameTextView)
        nameTextView.text = employee.name

        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        numberTextView.text = employee.number

        // Assuming profilePictureLink is a URL, you may need to use a library like Glide or Picasso to load the image
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        Glide.with(context)
            .load(employee.profilePicture)
            .placeholder(R.drawable.stock_profile_man1)
            .error(R.drawable.stock_profile_man1)
            .into(profileImageView)

        return itemView
    }
}
