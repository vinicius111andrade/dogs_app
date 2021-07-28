package com.vdemelo.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.vdemelo.dogs.R
import com.vdemelo.dogs.databinding.ItemDogBinding
import com.vdemelo.dogs.model.DogBreed
import com.vdemelo.dogs.util.getProgressDrawable
import com.vdemelo.dogs.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogClickListener {

    fun updateDogList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = DataBindingUtil.inflate<ItemDogBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_dog,
            parent,
            false
        )

        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dogBreed = dogsList[position]
        holder.view.listener = this
    }

    override fun onDogClicked(clickedView: View) {
        val uuid: Int = clickedView.dogId.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        action.dogUuid = uuid
        Navigation.findNavController(clickedView).navigate(action)
    }

    override fun getItemCount()= dogsList.size

    class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)
}
