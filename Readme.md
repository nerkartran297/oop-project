# OOP.20261-GroupNo – Sorting Algorithm Demonstration

Ứng dụng Java Swing minh họa **Selection Sort**, **Merge Sort** và **Shell Sort** trên mảng `int[]` thuần
(đề tài số 3 – "Demonstration of sorting algorithms on array").

## Thành viên và phân công

| Ký hiệu | Họ tên | MSSV | Phần đảm nhiệm | Tỷ lệ |
|---|---|---|---|---|
| A | _(điền)_ | _(điền)_ | _(điền)_ | _%_ |
| B | _(điền)_ | _(điền)_ | _(điền)_ | _%_ |
| C | _(điền)_ | _(điền)_ | _(điền)_ | _%_ |
| D | _(điền)_ | _(điền)_ | _(điền)_ | _%_ |
| E | _(điền)_ | _(điền)_ | _(điền)_ | _%_ |

Video demo: _(dán link)_

## Cấu trúc thư mục

```
design/        UseCase, GeneralClass, DetailClass (.asta và .png) – sinh bằng design/build.sh
report/        OOP.20261-GroupNo-Report.docx
presentation/  OOP.20261-GroupNo-Presentation.pptx và .pdf
sourcecode/    mã nguồn Java (src/model, src/view, src/controller, test/)
Readme.md
```

## Chạy chương trình

Cần JDK 17 trở lên (đã thử với JDK 21).

```bash
cd sourcecode
./run.sh          # biên dịch và chạy ứng dụng
./run.sh test     # chạy bộ test 3 thuật toán (không cần JUnit)
```

Không có script thì biên dịch thủ công:

```bash
cd sourcecode
mkdir -p build
javac -d build src/model/*.java src/view/*.java src/controller/*.java src/Main.java
java -cp build Main
```

## Cách dùng

1. Ở menu chính chọn một thuật toán (hoặc Help, Quit – có hộp thoại xác nhận).
2. Tạo mảng: nhập 1–20 số nguyên từ 0 đến 99, cách nhau bằng dấu phẩy hoặc khoảng trắng rồi bấm **Use input**,
   hoặc chọn kích thước rồi bấm **Random**.
3. Bấm **Start**, sau đó **Next / Previous** để xem từng bước, hoặc **Play** để chạy tự động (thanh Speed chỉnh tốc độ).
4. **Back** quay về menu chính ở mọi lúc.

## Thiết kế (tóm tắt)

| Gói | Lớp | Vai trò |
|---|---|---|
| model | `ArrayWrapper` | Bọc `int[]` thuần: `get`, `set`, `compare`, `swap`, `copy`; đếm số lần so sánh và ghi |
| model | `SortStep` | Ảnh chụp bất biến của một bước (mảng, chỉ số, khoảng, mảng phụ, mô tả) |
| model | `SortAlgorithm` (abstract) | Phần chung (Template Method): `run()`, `recordStep()`, `reset()`; phần riêng `sort()` |
| model | `SelectionSort`, `MergeSort`, `ShellSort` | Kế thừa `SortAlgorithm`, chỉ cài `sort()` |
| view | `MainMenuView`, `HelpView`, `DemoView`, `StepPanel` | Giao diện Swing |
| controller | `AppController` | Chuyển màn hình, kiểm tra đầu vào, điều khiển chạy từng bước; chỉ dùng kiểu `SortAlgorithm` (đa hình) |

Không dùng `ArrayList`, `Arrays.sort` hay lớp collection nào trong phần thuật toán.
Hai chỗ dùng thư viện chuẩn: `java.util.Random` (tạo mảng ngẫu nhiên) và `javax.swing` (giao diện).

## Nguồn tham khảo và công cụ hỗ trợ

_(Nhóm điền đầy đủ: video/tài liệu đã xem, và công cụ AI đã dùng nếu có. Đề yêu cầu khai báo rõ ràng.)_
