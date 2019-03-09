package mariusz.ambroziak.kassistant.model.jsp;

public class PhraseWithMultiplier {
	@Override
	public String toString() {
		return nazwa;
	}
	private String nazwa;
	private String url;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nazwa == null) ? 0 : nazwa.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhraseWithMultiplier other = (PhraseWithMultiplier) obj;
		if (nazwa == null) {
			if (other.nazwa != null)
				return false;
		} else if (!nazwa.equals(other.nazwa))
			return false;
		return true;
	}
	private float multiplier;
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	public float getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}
	public PhraseWithMultiplier(String nazwa) {
		super();
		this.nazwa = nazwa;
		this.multiplier=1;
	}
	
	
	
	
}
