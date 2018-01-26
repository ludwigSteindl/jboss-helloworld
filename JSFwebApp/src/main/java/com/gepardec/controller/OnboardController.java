package com.gepardec.controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.Part;
import javax.transaction.UserTransaction;

//import com.gepardec.model.User;
import com.gepardec.model.Cheetah;

@Named
@RequestScoped
public class OnboardController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String PATTERN_EMAIL = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	private static final String PATTERN_NAME  = "^[A-Za-z .'-]+$";
	private static final String PATTERN_PHONE = "^[+][0-9 ]+$";
	private static final String PATTERN_ADDRESS = "^[A-Za-z0-9 .,/'-]+$";
	private static final String PATTERN_BIRTHDAY = "^(0[1-9]|[12][0-9]|3[01])[-/.](0[1-9]|1[012])[-/.](19|20)\\d\\d$";

	public final static int MAX_IMAGE_LENGTH = 200;

	private final static Logger log = Logger.getLogger(OnboardController.class.toString());

	@PersistenceUnit
	private EntityManagerFactory emf;

	@Resource
	private UserTransaction ut;

	private Part part;

	@Inject
	private Cheetah cheetah;

	public Part getPart() {
		return this.part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public Cheetah getCheetah() {
		return this.cheetah;
	}

	public void setCheetah(Cheetah cheetah) {
		this.cheetah = cheetah;
	}

	public String persist(SigninController signinController) {

		try {
			ut.begin();
			EntityManager em = emf.createEntityManager();

			InputStream input = part.getInputStream();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[10240];

			for (int length = 0; (length = input.read(buffer)) > 0;) {
				output.write(buffer, 0, length);
			}
			cheetah.setPhoto(scale(output.toByteArray()));

			// TODO: CHECK IF USER HAS RIGHTS
			// User user = signinController.getUser();
			// user = em.find(User.class, user.getId());

			em.persist(cheetah);
			ut.commit();

			log.info("Added Cheetah " + cheetah);

			//FacesMessage m = new FacesMessage("Successfully added Cheetah!", "Cheetah has ID " + cheetah.getId());
			//FacesContext.getCurrentInstance().addMessage("onboardForm", m);

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), e.getCause().getMessage());
			FacesContext.getCurrentInstance().addMessage("onboardForm", fm);
		}
		return "search";
	}

	public byte[] scale(byte[] photo) throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(photo);
		BufferedImage originalBufferedImage = ImageIO.read(byteArrayInputStream);

		double originalWidth = (double) originalBufferedImage.getWidth();
		double originalHeight = (double) originalBufferedImage.getHeight();
		double relevantLength = originalWidth > originalHeight ? originalWidth : originalHeight;

		double transformationScale = MAX_IMAGE_LENGTH / relevantLength;
		int width = (int) Math.round(transformationScale * originalWidth);
		int height = (int) Math.round(transformationScale * originalHeight);

		BufferedImage resizedBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = resizedBufferedImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		AffineTransform affineTransform = AffineTransform.getScaleInstance(transformationScale, transformationScale);
		g2d.drawRenderedImage(originalBufferedImage, affineTransform);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(resizedBufferedImage, "PNG", baos);

		return baos.toByteArray();
	}

	public void isEmail(FacesContext fc, UIComponent uic, Object obj) throws ValidatorException {
		String value = (String) obj;

		if (!Pattern.matches(PATTERN_EMAIL, value)) {
			throw new ValidatorException(new FacesMessage());
		}
	}
	
	
	public void isName(FacesContext fc, UIComponent uic, Object obj) throws ValidatorException {
		String value = (String) obj;

		if (!Pattern.matches(PATTERN_NAME, value)) {
			throw new ValidatorException(new FacesMessage());
		}
	}
	
	public void isPhone(FacesContext fc, UIComponent uic, Object obj) throws ValidatorException {
		String value = (String) obj;

		if (!Pattern.matches(PATTERN_PHONE, value)) {
			throw new ValidatorException(new FacesMessage());
		}
	}
	
	public void isAddress(FacesContext fc, UIComponent uic, Object obj) throws ValidatorException {
		String value = (String) obj;
		
		if (!Pattern.matches(PATTERN_ADDRESS, value)) {
			throw new ValidatorException(new FacesMessage());
		}
	}
	
	public void isBirthday(FacesContext fc, UIComponent uic, Object obj) throws ValidatorException {
		String value = (String) obj;

		if (!Pattern.matches(PATTERN_BIRTHDAY, value)) {
			throw new ValidatorException(new FacesMessage());
		}
	}

}
