package com.sellnews.demo;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import io.dropwizard.hibernate.AbstractDAO;

public class EmployeDao extends AbstractDAO<Employe> {
	private final static Logger LOGGER = Logger.getLogger(EmployeDao.class.getName());

	public EmployeDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<Employe> findAll() {
		List<Employe> emp_details = new ArrayList<Employe>();
		try {
			emp_details = currentSession().createCriteria(Employe.class).add(Restrictions.ilike("emp_Status", "true"))
					.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
							.add(Projections.property("firstname"), "firstname")
							.add(Projections.property("phone"), "phone")
							.add(Projections.property("lastname"), "lastname")
							.add(Projections.property("e_mail"), "e_mail")
							.add(Projections.property("position"), "position"))
					.list();
			// LOGGER.info("Data is retrival");
		} catch (HibernateException exception) {
			LOGGER.error(exception);

		}
		return emp_details;
	}

	public String insert_empdetails(EmployePojo employepojo, String uuid) {
		try {

			Employe employe = new Employe();
			employe.setToken(uuid);
			if (employepojo.getE_mail() == null) {
				BeanUtils.copyProperties(employe, employepojo);
				employe.setEmp_registerDate(CurrentTimeStamp.generateTimeStamp());
				employe.setLastModified(CurrentTimeStamp.generateTimeStamp());
				currentSession().save(employe);
				LOGGER.info("data is entered");
				return ConstantVariabls.VALID_STATUS;
			} else if (VerificationClass.verificationOfEmail(employepojo.getE_mail())) {
				BeanUtils.copyProperties(employe, employepojo);
				employe.setEmp_registerDate(CurrentTimeStamp.generateTimeStamp());
				employe.setLastModified(CurrentTimeStamp.generateTimeStamp());
				currentSession().save(employe);
				LOGGER.info("data is entered");
				return ConstantVariabls.VALID_STATUS;
			} else {
				return ConstantVariabls.INVALID_EMAIL;
			}
		} catch (IllegalAccessException exception) {

			LOGGER.error(exception);
			return ConstantVariabls.ERRORS;
		} catch (InvocationTargetException exception) {
			// TODO Auto-generated catch block
			LOGGER.error(exception);
			return ConstantVariabls.ERRORS;
		}
	}

	public List getDataById(long id) throws Exception {
		Employe emp_details = null;
		EmployePojo employePojo = null;
		List list = currentSession().createCriteria(Employe.class).add(Restrictions.eq("id", id)).list();
		if (!list.isEmpty()) {
			emp_details = (Employe) list.get(0);
			list.clear();
			if ("true".equals(emp_details.getEmp_Status())) {
				if (emp_details.getToken() != null) {
					EmployePojo emp_detail = (EmployePojo) currentSession().createCriteria(Employe.class)
							.add(Restrictions.eq("id", id))
							.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
									.add(Projections.property("firstname"), "firstname")
									.add(Projections.property("phone"), "phone")
									.add(Projections.property("lastname"), "lastname")
									.add(Projections.property("e_mail"), "e_mail")
									.add(Projections.property("position"), "position"))
							.setResultTransformer(Transformers.aliasToBean(EmployePojo.class)).list().get(0);
					LOGGER.info(ConstantVariabls.DATA_FOUND);
					list.add(ConstantVariabls.DATA_FOUND);
					list.add(emp_detail);
					return list;
				} else {
					LOGGER.error(ConstantVariabls.LOGINERROR);
					list.add(ConstantVariabls.LOGINERROR);
					list.add(employePojo);
					return list;
				}
			} else {
				LOGGER.error(ConstantVariabls.USER_NOT_FOUND);
				list.add(ConstantVariabls.USER_NOT_FOUND);
				list.add(employePojo);

				return list;
			}
		} else {
			list.clear();
			list.add(ConstantVariabls.USER_NOT_FOUND);
			list.add(employePojo);
			return list;
		}

	}

	public String removeUser(Long id) {
		try {
			Employe employe = (Employe) currentSession().createCriteria(Employe.class).add(Restrictions.eq("id", id))
					.list().get(0);
			if (employe.getToken() != null) {
				if ("true".equals(employe.getEmp_Status())) {
					employe.setEmp_Status("false");
					employe.setToken(null);
					currentSession().update(employe);
					LOGGER.info("Data Sucessfully removed");
					return ConstantVariabls.VALID_STATUS;
				} else {
					return ConstantVariabls.USER_NOT_FOUND;
				}
			} else {
				return ConstantVariabls.UNAUTH;
			}
		} catch (Exception exception) {
			LOGGER.error(exception);
			return ConstantVariabls.ERRORS;
		}
	}

	public String update_User(Long id, EmployePojo new_employe) {
		try {
			Employe employe = (Employe) currentSession().createCriteria(Employe.class).add(Restrictions.eq("id", id))
					.list().get(0);
			if (employe.getToken() != null) {
				if (("true".equals(employe.getEmp_Status()))) {
					if (((new_employe.getE_mail() != null))) {
						if (VerificationClass.verificationOfEmail(new_employe.getE_mail()))
							employe.setE_mail(new_employe.getE_mail());
						else
							return ConstantVariabls.INVALID_EMAIL;
					}
					if ((new_employe.getFirstname() != null)) {
						employe.setFirstname(new_employe.getFirstname());
					}
					if ((new_employe.getLastname() != null)) {
						employe.setLastname(new_employe.getLastname());
					}
					if ((new_employe.getPhone() != null)) {
						employe.setPhone(new_employe.getPhone());
					}
					if ((new_employe.getPosition() != null)) {
						employe.setPosition(new_employe.getPosition());
					}
					LOGGER.info(CurrentTimeStamp.generateTimeStamp());
					employe.setLastModified(CurrentTimeStamp.generateTimeStamp());
					LOGGER.info(CurrentTimeStamp.generateTimeStamp());
					currentSession().update(employe);
					LOGGER.info("Data Sucessfully updated");
					return ConstantVariabls.VALID_STATUS;
				} else {
					return ConstantVariabls.USER_NOT_FOUND;
				}
			} else {
				return ConstantVariabls.LOGINERROR;
			}

		} catch (Exception exception) {
			LOGGER.error(exception);
			return ConstantVariabls.ERRORS;
		}
	}

	public List<Employe> getUser(LastModifiedData lastModifiedData) {
		List<Employe> employelist = null;
		try {
			/*
			 * employelist =
			 * currentSession().createCriteria(Employe.class).add(Restrictions.
			 * between("lastModified", lastModifiedData.getStartdate(),
			 * lastModifiedData.getLastdate())).list();
			 */
			employelist = currentSession().createCriteria(Employe.class)
					.add(Restrictions.gt("lastModified", lastModifiedData.getStartdate()))
					.add((Restrictions.lt("lastModified", lastModifiedData.getLastdate()))).list();
		} catch (HibernateException exception) {
			LOGGER.error(exception.getMessage());
			return employelist;
		}
		return employelist;
	}

	public String insertUUID(String uuid, long id, String email) throws Exception {
		Employe old_data = (Employe) currentSession().createCriteria(Employe.class).add(Restrictions.eq("id", id))
				.list().get(0);
		if ("true".equals(old_data.getEmp_Status())) {
			if ((email).equals(old_data.getE_mail())) {
				old_data.setToken(uuid);
				currentSession().update(old_data);
				return ConstantVariabls.VALID_STATUS;
			} else {
				return ConstantVariabls.DATA_MISMATCH;
			}
		} else {
			LOGGER.error(ConstantVariabls.USER_NOT_FOUND);
			return ConstantVariabls.USER_NOT_FOUND;
		}
	}

	public String logOut(long id, String email) throws Exception {
		Employe old_data = (Employe) currentSession().createCriteria(Employe.class).add(Restrictions.eq("id", id))
				.add(Restrictions.ilike("e_mail", email)).list().get(0);
		if ("true".equals(old_data.getEmp_Status())) {
			if (((email).equals(old_data.getE_mail())) && (old_data.getToken() != null)) {
				old_data.setToken(null);
				currentSession().update(old_data);
				return ConstantVariabls.VALID_STATUS;
			} else {
				return ConstantVariabls.DATA_MISMATCH;
			}
		} else {
			LOGGER.error(ConstantVariabls.USER_NOT_FOUND);
			return ConstantVariabls.USER_NOT_FOUND;
		}
	}
}
