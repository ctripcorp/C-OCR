C-OCR是携程自研的OCR项目，主要包括身份证、护照、火车票、签证等旅游相关证件、材料的识别。

项目包含4个部分，拒识、检测、识别、后处理。

C-OCR is a self-developed OCR project by Ctrip, mainly including the recognition of travel-related documents and materials such as ID cards, passports, train tickets, and visas.

The project consists of four parts: rejection, detection, recognition, and post-processing.

---
# Prerequisites 
## OpenCV
C-OCR uses OpenCV for optical recognition
### Installing OpenCV on IntelliJ
1. Download the newest release for your platform from [opencv.org](https://opencv.org/releases/)
2. Run the installer (If using Windows, the installer is not signed and Windows will require you to verify you want to run it)
3. Navigate to `opencv\build\java` and copy the entire folder to your project folder
4. In IntelliJ go to File > Project Structure > Dependencies
5. Click the + to add a new dependency and select Jar or Directories
6. Navigate to your project folder then select opencv-xxx.jar and click OK
7. Double-click the dependency you just added
8. Click the + and select the appropriate DLL from either the x64 or x86 library depending on your platform
9. Click OK then finally Apply
10. To check if OpenCV has been installed properly run the following
```
import org.opencv.core.Core;
public class test
{
  public static void main(String[] args)
  {
    System.out.println(Core.VERSION);
  }
}
