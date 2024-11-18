package vn.edu.hust.studentman

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(private val students: MutableList<StudentModel>) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item, parent, false)
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId

    holder.imageEdit.setOnClickListener {
      showUpdateStudentDialog(holder, position)
    }

    holder.imageRemove.setOnClickListener {
      showDeleteStudentDialog(holder, position)
    }
  }

  private fun showUpdateStudentDialog(holder: StudentViewHolder, position: Int) {
    val student = students[position]
    val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_update_student, null)
    val dialog = AlertDialog.Builder(holder.itemView.context)
      .setTitle("Update Student")
      .setView(dialogView)
      .setPositiveButton("Update") { _, _ ->
        val name = dialogView.findViewById<EditText>(R.id.edit_student_name).text.toString()
        val id = dialogView.findViewById<EditText>(R.id.edit_student_id).text.toString()
        if (name.isNotEmpty() && id.isNotEmpty()) {
          students[position] = StudentModel(name, id)
          notifyItemChanged(position)
        }
      }
      .setNegativeButton("Cancel", null)
      .create()
    dialogView.findViewById<EditText>(R.id.edit_student_name).setText(student.studentName)
    dialogView.findViewById<EditText>(R.id.edit_student_id).setText(student.studentId)
    dialog.show()
  }

  private fun showDeleteStudentDialog(holder: StudentViewHolder, position: Int) {
    val student = students[position]
    val dialog = AlertDialog.Builder(holder.itemView.context)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete ${student.studentName}?")
      .setPositiveButton("Delete") { _, _ ->
        students.removeAt(position)
        notifyItemRemoved(position)
        // Show snackbar to undo
        Snackbar.make(holder.itemView, "${student.studentName} deleted", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            students.add(position, student)
            notifyItemInserted(position)
          }.show()
      }
      .setNegativeButton("Cancel", null)
      .create()
    dialog.show()
  }
}