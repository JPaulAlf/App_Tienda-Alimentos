/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.sun.xml.registry.uddi.bindings_v2_2.Email;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author juand
 */
public class Correo {

    private final String correo = "soundevutn@gmail.com";
    private final String contrasenna = "lmtpfrnpidltshkp";

    public Correo() {

    }

    public void enviarCorreoCompra(Transaccion pedido) {
        Properties propiedad = new Properties();
        propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
        propiedad.setProperty("mail.smtp.starttls.enable", "true");
        propiedad.setProperty("mail.smtp.port", "587");
        propiedad.setProperty("mail.smtp.auth", "true");

        Session sesion = Session.getDefaultInstance(propiedad);
        String correoEnvia = correo;
        String contrasena = contrasenna;
        String receptor = pedido.getCliente().getCorreo();

        String asunto = "GRACIAS POR SU COMPRA";

        String mensaje = "<div  style=\"  display: block;  width: 70vw; background-color: #F8F1E5;\">\n"
                + "     <h1 style=\" text-align: center; text-decoration: none; color: #2F3131;\">HOME SHOP</h1>\n"
                + "\n"
                + "      <h2 style=\" text-align: center; color: #426E86;\">Usted ha realizado un pedido ha HOME SHOP</h2>\n"
                + "      <table   style=\"margin-left: 23vw; text-align: left; border: solid 5px #426E86;\">\n"
                + "         <thead >\n"
                + "             <tr>\n"
                + "                 <th scope=\"col\">Nombre</th>\n"
                + "                 <th scope=\"col\">Cantidad</th>\n"
                + "                 <th scope=\"col\">Precio</th>\n"
                + "                 <th scope=\"col\">Total</th>\n"
                + "             </tr>\n"
                + "         </thead>\n"
                + "         <tbody>\n";

        for (Producto producto : pedido.getListaProductos()) {
            mensaje += "<tr style=\"border: solid 2px #426E86;\">\n"
                    + "                 <th style=\"border: solid 2px #426E86;\"scope=\"row\">" + producto.getNombre() + "</th>\n"
                    + "                 <td style=\"border: solid 2px #426E86;text-align: center\">" + producto.getCantComprada() + "</td>\n"
                    + "                 <td style=\"border: solid 2px #426E86;\">" + producto.getPrecio_Mascara() + "</td>\n"
                    + "                 <td style=\"border: solid 2px #426E86;\">" + producto.getTotalCantComprada_Mascara() + "</td>\n"
                    + "             </tr>";
        }
        mensaje
                += "             <!-- fin de cliclo -->\n"
                + "\n"
                + "         </tbody>\n"
                + "     </table>\n"
                + "     <ul style=\"  color: #F8F1E5; background-color: #426E86; list-style: none; text-align: left;\">\n"
                + "        <li> <p style=\"display: inline; font-weight: bolder;\">Fecha estimada:</p> " + pedido.getFecEstimada_Mascara() + "</li>\n"
                + "        <li> <p style=\"display: inline; font-weight: bolder;\">Subtotal:</p>   " + pedido.getSubtotal_Mascara() + "</li>\n"
                + "        <li><p style=\"display: inline; font-weight: bolder;\">Descuento:</p>" + pedido.getDescuentoMonto_Mascara() + "</li>\n"
                + "        <li><p style=\"display: inline; font-weight: bolder;\">Impuesto:</p>  " + pedido.getImpuestoMonto_Mascara() + "</li>\n"
                + "        <li><p style=\"display: inline; font-weight: bolder;\">Total:</p>  " + pedido.getTotal_Mascara() + "</li>\n"
                + "     </ul>\n"
                + "     <h3 style=\"color: #2F3131;\">Gracias por su preferencia " + pedido.getCliente().getNombre() + ", recibirá su paquete en 5 días hábiles</h3>\n"
                + "   </div>";

        MimeMessage mail = new MimeMessage(sesion);
        try {
            mail.setFrom(new InternetAddress(correoEnvia));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
            mail.setSubject(asunto);
            mail.setText(mensaje, "utf-8", "html");

            Transport transportar = sesion.getTransport("smtp");
            transportar.connect(correoEnvia, contrasena);
            transportar.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transportar.close();

        } catch (Exception e) {

        }

    }

    public void enviarCorreo_UsuarioRegistrado(Cliente pCliente) {
        Properties propiedad = new Properties();
        propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
        propiedad.setProperty("mail.smtp.starttls.enable", "true");
        propiedad.setProperty("mail.smtp.port", "587");
        propiedad.setProperty("mail.smtp.auth", "true");

        Session sesion = Session.getDefaultInstance(propiedad);
        String correoEnvia = correo;
        String contrasena = contrasenna;
//       String receptor = pedido.getCliente().getCorreo();
        String receptor = pCliente.getCorreo();
        String asunto = "Su registro en Home Shop ha sido efectuado";

        String mensaje = "<!-- MENSAJE DE REGISTRO -->\n"
                + "<div>"
                + "     <h3 style=\"color: #2F3131;\">Recibira un correo con los siguientes datos, cuando un trabajador revise su solicitud. </h3>"
                + "     <ul style=\"text-align: left;\">\n"
                + "        <li> <p style=\"display: inline; font-weight: bolder;\">Correo electronico: </p> " + pCliente.getCorreo() + "</li>\n"
                + "        <li> <p style=\"display: inline; font-weight: bolder;\">Contraseña: * * * * * * * *</p></li>\n"
                + "     </ul>\n"
                + "     <h3 style=\"color: #2F3131;\">Gracias por su preferencia </h3>"
                + "   </div>";

        MimeMessage mail = new MimeMessage(sesion);
        try {
            mail.setFrom(new InternetAddress(correoEnvia));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
            mail.setSubject(asunto);
            mail.setText(mensaje, "utf-8", "html");

            Transport transportar = sesion.getTransport("smtp");
            transportar.connect(correoEnvia, contrasena);
            transportar.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transportar.close();

        } catch (Exception e) {

        }

    }

    public void enviarCorreo_UsuarioAprobado(Cliente pCliente) {
        Properties propiedad = new Properties();
        propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
        propiedad.setProperty("mail.smtp.starttls.enable", "true");
        propiedad.setProperty("mail.smtp.port", "587");
        propiedad.setProperty("mail.smtp.auth", "true");

        Session sesion = Session.getDefaultInstance(propiedad);
        String correoEnvia = correo;
        String contrasena = contrasenna;
//       String receptor = pedido.getCliente().getCorreo();
        String receptor = pCliente.getCorreo();
        String asunto = "Su registro en Home Shop ha sido procesado";

        String mensaje = "<!-- MENSAJE DE APROBACION -->\n"
                + "<div>"
                + "     <h3 style=\"color: #2F3131;\">Felicidades, ha sido aprobado para realizar compras con nosotros. </h3>"
                + "     <h3 style=\"color: #2F3131;\">Con el siguiente correo y su contraseña ingresada, podra realizar compras que son llevadas hasta su puerta</h3>"
                + "     <h3 style=\"color: #2F3131;\">Y lo mejor de ello, no debes salir de casa</h3>"
                + "     <ul style=\"text-align: left;\">\n"
                + "        <li> <p style=\"display: inline; font-weight: bolder;\">Correo electronico: </p> " + pCliente.getCorreo() + "</li>\n"
                + "        <li> <p style=\"display: inline; font-weight: bolder;\">Contraseña: </p>   " + pCliente.getContrasenia() + "</li>\n"
                + "     </ul>\n"
                + "     <h3 style=\"color: #2F3131;\">Gracias por su preferencia </h3>"
                + "   </div>";

        MimeMessage mail = new MimeMessage(sesion);
        try {
            mail.setFrom(new InternetAddress(correoEnvia));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
            mail.setSubject(asunto);
            mail.setText(mensaje, "utf-8", "html");

            Transport transportar = sesion.getTransport("smtp");
            transportar.connect(correoEnvia, contrasena);
            transportar.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transportar.close();

        } catch (Exception e) {

        }

    }

    public void enviarCorreo_UsuarioDenegado(Cliente pCliente) {
        Properties propiedad = new Properties();
        propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
        propiedad.setProperty("mail.smtp.starttls.enable", "true");
        propiedad.setProperty("mail.smtp.port", "587");
        propiedad.setProperty("mail.smtp.auth", "true");

        Session sesion = Session.getDefaultInstance(propiedad);
        String correoEnvia = correo;
        String contrasena = contrasenna;
//       String receptor = pedido.getCliente().getCorreo();
        String receptor = pCliente.getCorreo();
        String asunto = "Su registro en Home Shop ha sido procesado";

        String mensaje = "<!-- MENSAJE DE DENEGADO -->\n"
                + "<div>"
                + "     <h3 style=\"color: #2F3131;\">Lo sentimos, nuestros servicios aun no cubren su ubicacion especificada </h3>"
                + "     <h3 style=\"color: #2F3131;\">Por lo tanto su solicitud de registro ha sido denegada </h3>"
                + "     <h3 style=\"color: #2F3131;\">Las direcciones suiministradas fueron: </h3>";
        int num = 1;
        for (Direccion itemDireccion : pCliente.getPosDirecciones()) {
            mensaje += "     <ul style=\"text-align: left;\">\n"
                    + "        <li> <p style=\"display: inline; font-weight: bolder;\">Direccion Numero: </p> " + num + "</li>\n"
                    + "        <li> <p style=\"display: inline; font-weight: bolder;\">Provincia: </p> " + itemDireccion.getProvincia() + "</li>\n"
                    + "        <li> <p style=\"display: inline; font-weight: bolder;\">Canton: </p>   " + itemDireccion.getCanton() + "</li>\n"
                    + "        <li><p style=\"display: inline; font-weight: bolder;\">Distrito: </p>" + itemDireccion.getDistrito() + "</li>\n"
                    + "        <li><p style=\"display: inline; font-weight: bolder;\">Barrio: </p>" + itemDireccion.getBarrio() + "</li>\n"
                    + "     </ul>\n";
            num++;
        }

        mensaje += "     </ul>\n"
                + "     <h3 style=\"color: #2F3131;\">Gracias y nos vemos pronto </h3>"
                + "   </div>";

        MimeMessage mail = new MimeMessage(sesion);
        try {
            mail.setFrom(new InternetAddress(correoEnvia));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
            mail.setSubject(asunto);
            mail.setText(mensaje, "utf-8", "html");

            Transport transportar = sesion.getTransport("smtp");
            transportar.connect(correoEnvia, contrasena);
            transportar.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transportar.close();

        } catch (Exception e) {

        }

    }

}
