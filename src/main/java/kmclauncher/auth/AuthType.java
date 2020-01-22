package kmclauncher.auth;

public enum AuthType
{
    AUTH ("authenticate"),
    REFRESH ("refresh"),
    VALIDATE ("validate");

    private String name = "";
         
    //Constructeur
    AuthType(String name){
      this.name = name;
    }
     
    public String toString(){
      return name;
    }
}