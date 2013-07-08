<%@include file="_header.jsp"%><%!

private String getRGB(float t) {
	
	if (t < -20) {
		return "rgb(128,128,128)";
	}
	final float MIN_T = 0; final float MAX_T = 1; // outside
	//final float MIN_T = 12; final float MAX_T = 22; // inside

	int[] rc = {0,0,     0,    255, 255};
	int[] gc = {0,0,   255,  255,   0};
	int[] bc = {0,255, 255,     0,   0};
	int nc = rc.length;
	float R = (MAX_T - MIN_T) / (float)(nc-1);
	int r,g,b;

	int index = (int) ((t - MIN_T) / R);
	if (index < 0) {
		index = 0;
		r = rc[0]; g = gc[0]; b = bc[0];
	} else if (index >= rc.length-1 ) {
		index = rc.length-2;
		r = rc[nc-1]; g = gc[nc-1]; b = bc[nc-1];
	} else {

		float tr = (t - MIN_T) /R;
		tr -= (float)(int)tr;
		r = rc[index] + (int)(tr * (float)(rc[index+1] - rc[index]));
		g = gc[index] + (int)(tr * (float)(gc[index+1] - gc[index]));
		b = bc[index] + (int)(tr * (float)(bc[index+1] - bc[index]));
	}
	return "rgb(" + r + "," + g + "," + b + ")";
}
%>
<svg width="1000" height="1000" >
<%
	int N = 128;
	int[][] hm = new int[N][N];
	double x0 = -10.5;
	double y0 = 51	;
	double width = 4;
	double height = 4;
	
	double xscale = N / width;
	double yscale = N / height;
	
	int i,j,x,y,w,h,xe,ye,max=0;
	for (HeatMapRecord r : (List<HeatMapRecord>)hsession.createQuery("from HeatMapRecord where id > 300000 and id < 900000").list()) {
	
		x = (int) ((r.getSwLongitude() - x0 )* xscale);
		y = (int) ((r.getSwLatitude() - y0) * yscale);
		w = (int) ((r.getNeLongitude() - r.getSwLongitude()) * xscale);
		h = (int) ((r.getNeLatitude() - r.getSwLatitude()) * yscale);

		if (w > 40 || h > 40) {
			continue;
		}
		
		if ( x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		xe = x + w;
		if (xe >= N) {
			xe = N-1;
		}
		ye = y + h;
		if (ye >= N) {
			ye = N-1;
		}
		for (j = y; j < ye; j++) {
			for (i = x; i < xe; i++) {
				hm[j][i]++;
				if (hm[j][i]>max) {
					max = hm[j][i];
				}
			}
		}
		//out.write ("<rect x1='" + x1 + "' y1='" + y1 + "' width='" + w + "' height='" + h + "' ");
		//out.write (" fill='none' stroke='black' fill-opacity='0.01' ");
		//out.write (" />\n");
	}
	for (j = 0; j < N; j++) {
		for (i = 0; i < N; i++) {
			
			out.write ("<rect x='" + i*4 + "' y='" + (1000-j*5) + "' width='4' height='5' ");
			out.write (" stroke='none'  ");
			out.write (" fill='" + getRGB((float)hm[j][i]/(float)max) + "' ");
			out.write (" />\n");
		}
	}
%>
</svg>
<% 
//out.write ("max=" + max);
	tx.commit();
	HibernateUtil.closeSession();
%>