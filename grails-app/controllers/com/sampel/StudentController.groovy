package com.sampel

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.XML
import grails.converters.JSON

class StudentController {

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    static allowedMethods = [save: "POST", update: ["POST", "PUT"], delete: ["POST", "DELETE"]]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        //[studentInstanceList: Student.list(params), studentInstanceTotal: Student.count()]
	def studentInstanceList = Student.list(params)
	def studentInstanceTotal = Student.count()
	//render studentInstanceList as JSON

	withFormat{
	    html {[studentInstanceList:studentInstanceList, studentInstanceTotal:studentInstanceTotal]}
	    json {render studentInstanceList as JSON}
	    xml {render studentInstanceList as XML}
	}
	
    }

    def create() {
        [studentInstance: new Student(params)]
    }

    def save() {
	println params
        //def studentInstance = new Student(params)
	def studentInstance = new Student(request.format=='xml' ? params.student : params)
        if (studentInstance.save(flush: true)) {
            //render(view: "create", model: [studentInstance: studentInstance])
            withFormat{
		html {
			redirect(action: "show", id: studentInstance.id)
		}
	        json {
			response.status = 201
			render studentInstance as JSON
		}
	        xml {
			response.status = 201
			render studentInstance as XML
		}
            }            
        } else {
		withFormat{
			html {
				redirect(view: "create", models:[studentInstance:studentInstance])
			}
			json {
				response.status = 403
				render studentInstance.errors as JSON
			}
			xml {
				response.status = 403
				render studentInstance.errors as XML
			}
		    }
	}

        //flash.message = message(code: 'default.created.message', args: [message(code: 'student.label', default: 'Student'), studentInstance.id])
        //redirect(action: "show", id: studentInstance.id)
    }

    def show(Long id) {
        def studentInstance = Student.get(id)
        if (!studentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "list")
            return
        }

        //[studentInstance: studentInstance]
	//render studentInstance as JSON
	withFormat{
	    html {[studentInstance:studentInstance]}
	    json {render studentInstance as JSON}
	    xml {render studentInstance as XML}
	}
    }

    def edit(Long id) {
        def studentInstance = Student.get(id)
        if (!studentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "list")
            return
        }

        [studentInstance: studentInstance]
    }

    //def update(Long id, Long version) {
    def update() {
        def id = params.id
        def studentInstance = Student.get(id)
        if (!studentInstance) {
	    withFormat{
		    html {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'student.label', default: 'Student'), id])
		        redirect(action: "list")
		    }
		    //json {render studentInstance as JSON}
		    //xml {render studentInstance as XML}

		    json {response.sendError(404)}
		    xml {response.sendError(404)}
	    }
            
            return
        }

	if(params.version) {
		def version = params.version.toLong()
		if (version != null) {
		    if (studentInstance.version > version) {
			studentInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
				  [message(code: 'student.label', default: 'Student')] as Object[],
				  "Another user has updated this Student while you were editing")
			//render(view: "edit", model: [studentInstance: studentInstance])
			withFormat{
				    html {
					render(view: "edit", model: [studentInstance: studentInstance])
				    }
				    json {response.sendError(409)}
				    xml {response.sendError(409)}
                        }
			return
		    }
		}
	}

        studentInstance.properties = params

        if (!studentInstance.save(flush: true)) {
            //render(view: "edit", model: [studentInstance: studentInstance])
	    withFormat{
		html {
			render(view: "edit", model: [studentInstance: studentInstance])
		}
		json {
			response.status = 403
			render studentInstance.errors as JSON
		}
		xml {
			response.status = 403
			render studentInstance.errors as XML
		}
	    }
            return
        }

        //flash.message = message(code: 'default.updated.message', args: [message(code: 'student.label', default: 'Student'), studentInstance.id])
        //redirect(action: "show", id: studentInstance.id)

	withFormat{
		html {
			flash.message = message(code: 'default.updated.message', args: 
						[
							message(code: 'student.label', default: 'Student'), 
							studentInstance.id
						])
			redirect(action: "show", id: studentInstance.id)
		}
		json {
			response.status = 204
			render studentInstance as JSON
		}
		xml {
			response.status = 204
			render ''
		}
	    }
    }

    def delete(Long id) {
	println params
        def studentInstance = Student.get(id)
        if (!studentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "list")
            return
        }

        try {
            studentInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "show", id: id)
        }
    }
}
