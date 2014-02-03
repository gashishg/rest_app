import com.sampel.Student
class BootStrap {

    def init = { servletContext ->
	// Create students - crude error checking for now
	def student = new Student(name:"Ansh Gupta", age:11)
	student.save()
	student = new Student(name:"Anshool Yadav", age:11)
	student.save()
	if(student.hasErrors()) {
	     println student.errors
	}
    }
    def destroy = {
    }
}
