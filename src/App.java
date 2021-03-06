import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Base64;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        while (true) {
            System.out.println("Enter the path of a certificate (.cer) file: ");

            // pass the path to the file as a parameter
            File file = new File(reader.nextLine());

            Scanner sc = null;
            try {
                sc = new Scanner(file);

                String payload = "";
                while (sc.hasNextLine()) {
                    String nLine = sc.nextLine();
                    if (nLine.trim().startsWith("-----"))
                        continue;
                    payload += nLine;
                }

                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                // verify payload string is valid X.509 certificate
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Certificate cert = cf.generateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(payload)));

                byte[] hash = digest.digest(cert.getEncoded());
                System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(hash).toLowerCase());

            } catch (Exception e) {
                System.out.println("Failed to init");
            } finally {
                try {
                    sc.close();
                } catch (Exception e) {
                    System.out.println("Failed to close file");
                }
            }
        }
        //once finished
        //reader.close();
    }
}

