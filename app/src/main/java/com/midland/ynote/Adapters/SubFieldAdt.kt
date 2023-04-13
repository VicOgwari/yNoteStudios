package com.midland.ynote.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.midland.ynote.R
import com.midland.ynote.Objects.SelectedDoc
import com.midland.ynote.Utilities.FilingSystem
import java.util.*

var documentTags = ArrayList<String>()
//private val documentAdapter: DocumentAdapter
//    get() {
//        TODO()
//    }


class SubFieldAdt(private val c: Context, private val subFields: java.util.ArrayList<String>,
                  private val cloudVideosAdapter: CloudVideosAdapter?, private val documentAdapter: DocumentAdapter?
):
        RecyclerView.Adapter<SubFieldAdt.CheckSubFieldVH>(), Filterable {

    var tagPosition = 0

    class CheckSubFieldVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subFieldCheck: CheckBox = itemView.findViewById(R.id.subFieldCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckSubFieldVH {
        val v = LayoutInflater.from(c).inflate(R.layout.check_subfield, parent, false)
        return CheckSubFieldVH(v)
    }

    override fun onBindViewHolder(holder: CheckSubFieldVH, position: Int) {
        FilingSystem.selectedSubFields = ArrayList()
        tagPosition = holder.absoluteAdapterPosition
        val s = subFields[tagPosition]
        documentTags = ArrayList(subFields)
        holder.subFieldCheck.text = s

            holder.subFieldCheck.setOnCheckedChangeListener { buttonView, isChecked ->
//                buttonView.isChecked = s == buttonView.text
                if (isChecked){
                    FilingSystem.selectedSubFields.add(s)
                    if (documentAdapter != null && cloudVideosAdapter == null) {
                        for (sub in FilingSystem.selectedSubFields){
                            documentAdapter.getFilter().filter(sub)
                            // TODO: 12/10/2021 Tengeneza filter 
                        }
                    }else
                        if (cloudVideosAdapter != null && documentAdapter == null){
                        for (sub in FilingSystem.selectedSubFields){
                            cloudVideosAdapter.getFilter().filter(sub)
                        }
                    }
                }
                else{
                    FilingSystem.selectedSubFields.remove(s)
                    if (documentAdapter != null && cloudVideosAdapter == null) {
                        for (sub in FilingSystem.selectedSubFields){
                            documentAdapter.getFilter().filter(sub)
                        }
                    }else if (cloudVideosAdapter != null && documentAdapter == null){
                        for (sub in FilingSystem.selectedSubFields){
                            cloudVideosAdapter.getFilter().filter(sub)
                        }
                    }
                }

            }


    }

    override fun getItemCount(): Int {
        return subFields.size
    }

    override fun getFilter(): Filter? {
        return filter
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val yearFilteredDocs: List<SelectedDoc> = java.util.ArrayList()
            val stringFilteredTags: MutableList<String> = java.util.ArrayList()
            //            if (DocRetrieval.Companion.getYearFilterString().equals("All")) {
            if (constraint.isEmpty()) {
                stringFilteredTags.addAll(documentTags)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                for (tag in documentTags) {
                    if (tag.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        stringFilteredTags.add(tag)
                    }
                }
            }

            //            }
//            else {
//                if (constraint == null || constraint.length() == 0) {
//                    for (SelectedDoc doc : documentsList) {
//                        if (doc.getDocMetaData().split("_-_")[2].toLowerCase().contains(DocRetrieval.Companion.getYearFilterString())) {
//                            yearFilteredDocs.add(doc);
//                        }
//                    }
//                    stringFilteredDocs.addAll(yearFilteredDocs);
//                } else {
//                    String filterPattern = constraint.toString().toLowerCase().trim();
//                    for (SelectedDoc doc : yearFilteredDocs) {
//                        if (doc.getDocMetaData().split("_-_")[5].toLowerCase().startsWith(filterPattern)) {
//                            stringFilteredDocs.add(doc);
//                        }
//                    }
//
//                }
//            }
            val results = FilterResults()
            results.values = stringFilteredTags
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            subFields.clear()
            subFields.addAll(results.values as Collection<String>)
            notifyDataSetChanged()
        }
    }
}