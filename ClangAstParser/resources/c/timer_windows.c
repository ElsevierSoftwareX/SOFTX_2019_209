#include <time.h>
#include <windows.h>
#include <stdio.h>

double bar() {
   
   return 1.0;
}


double foo() {
   double a = 0;
   for(int i = 0; i < 1000; i++) {
      LARGE_INTEGER clava_timing_start_0, clava_timing_end_0, clava_timing_frequency_0;
      QueryPerformanceFrequency(&clava_timing_frequency_0);
      QueryPerformanceCounter(&clava_timing_start_0);
      a += bar();
      QueryPerformanceCounter(&clava_timing_end_0);
      double clava_timing_durantion_0 = (clava_timing_end_0.QuadPart-clava_timing_start_0.QuadPart) / (double)clava_timing_frequency_0.QuadPart;;
      printf("Time:%fms\n", clava_timing_durantion_0);
   }
   
   return a;
}


int main() {
   LARGE_INTEGER clava_timing_start_1, clava_timing_end_1, clava_timing_frequency_1;
   QueryPerformanceFrequency(&clava_timing_frequency_1);
   QueryPerformanceCounter(&clava_timing_start_1);
   foo();
   QueryPerformanceCounter(&clava_timing_end_1);
   double clava_timing_durantion_1 = (clava_timing_end_1.QuadPart-clava_timing_start_1.QuadPart) / (double)clava_timing_frequency_1.QuadPart;;
   printf("Time:%fms\n", clava_timing_durantion_1);
}
