package co.edu.uco.ucochallenge.secondary.adapters.repository.entity;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "tipo_identificacion_id", nullable = false)
    private IdTypeEntity idType;

    @Column(name = "numero_identificacion", length = 25)
    private String idNumber;

    @Column(name = "primer_nombre", length = 20)
    private String firstName;

    @Column(name = "segundo_nombre", length = 25)
    private String secondName;

    @Column(name = "primer_apellido", length = 20)
    private String firstSurname;

    @Column(name = "segundo_apellido", length = 20)
    private String secondSurname;

    @ManyToOne
    @JoinColumn(name = "ciudad_id", nullable = false)
    private CityEntity homeCity;

    @Column(name = "correo_electronico", length = 250)
    private String email;

    @Column(name = "numero_telefono_movil", length = 20)
    private String mobileNumber;

    @Column(name = "correo_electronico_confirmado")
    private boolean emailConfirmed;

    @Column(name = "numero_telefono_movil_confirmado")
    private boolean mobileNumberConfirmed;

	private boolean emailConfirmedIsDefaultValue;

	private boolean mobileNumberConfirmedIsDefaultValue;

	public UserEntity() {
		setId(UUIDHelper.getDefault());
		setIdType(new IdTypeEntity());
		setIdNumber(TextHelper.getDefault());
		setFirstName(TextHelper.getDefault());
		setSecondName(TextHelper.getDefault());
		setFirstSurname(TextHelper.getDefault());
		setSecondSurname(TextHelper.getDefault());
		setHomeCity(new CityEntity());
		setEmail(TextHelper.getDefault());
		setMobileNumber(TextHelper.getDefault());
		setEmailConfirmed(false);
		setMobileNumberConfirmed(false);
		setEmailConfirmedIsDefaultValue(true);
		setMobileNumberConfirmedIsDefaultValue(true);
	}

	public UserEntity(final Builder builder) {
		setId(builder.id);
		setIdType(builder.idType);
		setIdNumber(builder.idNumber);
		setFirstName(builder.firstName);
		setSecondName(builder.secondName);
		setFirstSurname(builder.firstSurname);
		setSecondSurname(builder.secondSurname);
		setHomeCity(builder.homeCity);
		setEmail(builder.email);
		setMobileNumber(builder.mobileNumber);
		setEmailConfirmed(builder.emailConfirmed);
		setMobileNumberConfirmed(builder.mobileNumberConfirmed);
		setEmailConfirmedIsDefaultValue(builder.emailConfirmedIsDefaultValue);
		setMobileNumberConfirmedIsDefaultValue(builder.mobileNumberConfirmedIsDefaultValue);
	}

	public static final class Builder {

		private UUID id;
		private IdTypeEntity idType;
		private String idNumber;
		private String firstName;
		private String secondName;
		private String firstSurname;
		private String secondSurname;
		private CityEntity homeCity;
		private String email;
		private String mobileNumber;
		private boolean emailConfirmed;
		private boolean mobileNumberConfirmed;
		private boolean emailConfirmedIsDefaultValue = true;
		private boolean mobileNumberConfirmedIsDefaultValue = true;

		public Builder id(final UUID id) {
			this.id = id;
			return this;
		}

		public Builder idType(final IdTypeEntity idType) {
			this.idType = idType;
			return this;
		}

		public Builder idNumber(final String idNumber) {
			this.idNumber = idNumber;
			return this;
		}

		public Builder firstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder secondName(final String secondName) {
			this.secondName = secondName;
			return this;
		}

		public Builder firstSurname(final String firstSurname) {
			this.firstSurname = firstSurname;
			return this;
		}

		public Builder secondSurname(final String secondSurname) {
			this.secondSurname = secondSurname;
			return this;
		}

		public Builder homeCity(final CityEntity homeCity) {
			this.homeCity = homeCity;
			return this;
		}

		public Builder email(final String email) {
			this.email = email;
			return this;
		}

		public Builder mobileNumber(final String mobileNumber) {
			this.mobileNumber = mobileNumber;
			return this;
		}

		public Builder emailConfirmed(final boolean emailConfirmed) {
			this.emailConfirmed = emailConfirmed;
			this.emailConfirmedIsDefaultValue = false;
			return this;
		}

		public Builder mobileNumberConfirmed(final boolean mobileNumberConfirmed) {
			this.mobileNumberConfirmed = mobileNumberConfirmed;
			this.mobileNumberConfirmedIsDefaultValue = false;
			return this;
		}

		public UserEntity build() {
			return new UserEntity(this);
		}
	}

	public UUID getId() {
		return id;
	}

	public IdTypeEntity getIdType() {
		return idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public String getFirstSurname() {
		return firstSurname;
	}

	public String getSecondSurname() {
		return secondSurname;
	}

	public CityEntity getHomeCity() {
		return homeCity;
	}

	public String getEmail() {
		return email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public boolean isMobileNumberConfirmed() {
		return mobileNumberConfirmed;
	}

	public boolean isEmailConfirmedIsDefaultValue() {
		return emailConfirmedIsDefaultValue;
	}

	public boolean isMobileNumberConfirmedIsDefaultValue() {
		return mobileNumberConfirmedIsDefaultValue;
	}

	public void setId(final UUID id) {
		this.id = UUIDHelper.getDefault(id);
	}

	public void setIdType(final IdTypeEntity idType) {
		this.idType = ObjectHelper.getDefault(idType, new IdTypeEntity());
	}

	public void setIdNumber(final String idNumber) {
		this.idNumber = TextHelper.getDefaultWithTrim(idNumber);
	}

	public void setFirstName(final String firstName) {
		this.firstName = TextHelper.getDefaultWithTrim(firstName);
	}

	public void setSecondName(final String secondName) {
		this.secondName = TextHelper.getDefaultWithTrim(secondName);
	}

	public void setFirstSurname(final String firstSurname) {
		this.firstSurname = TextHelper.getDefaultWithTrim(firstSurname);
	}

	public void setSecondSurname(final String secondSurname) {
		this.secondSurname = TextHelper.getDefaultWithTrim(secondSurname);
	}

	public void setHomeCity(final CityEntity homeCity) {
		this.homeCity = ObjectHelper.getDefault(homeCity, new CityEntity());
	}

	public void setEmail(final String email) {
		this.email = TextHelper.getDefaultWithTrim(email);
	}

	public void setMobileNumber(final String mobileNumber) {
		this.mobileNumber = TextHelper.getDefaultWithTrim(mobileNumber);
	}

	public void setEmailConfirmed(final boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public void setMobileNumberConfirmed(final boolean mobileNumberConfirmed) {
		this.mobileNumberConfirmed = mobileNumberConfirmed;
	}

	public void setEmailConfirmedIsDefaultValue(final boolean emailConfirmedIsDefaultValue) {
		this.emailConfirmedIsDefaultValue = emailConfirmedIsDefaultValue;
	}

	public void setMobileNumberConfirmedIsDefaultValue(final boolean mobileNumberConfirmedIsDefaultValue) {
		this.mobileNumberConfirmedIsDefaultValue = mobileNumberConfirmedIsDefaultValue;
	}
}