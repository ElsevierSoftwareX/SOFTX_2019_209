#include <stdio.h>

void foo_input_2darray(const double I[2][3])
{
	printf("MomentsOfInertia\n");
	printf("%lf %lf %lf\n", I[0][0], I[0][1], I[0][2]);
	printf("%lf %lf %lf\n", I[1][0], I[1][1], I[1][2]);
}

void foo_incomplete_array(double A[][3])
{
}

int main() {
		
	return 0;
}