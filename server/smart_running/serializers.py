from rest_auth.serializers import UserDetailsSerializer
from rest_framework import serializers

from smart_running.models import Route, Marker


class RouteSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Route
        fields = ('url', 'publisher', 'date_published', 'last_updated', 'title', 'description',
                  'mode', 'difficulty', 'markers')  # markers too
        read_only_fields = ('publisher', 'date_published', 'last_updated')
        # TODO set read only fields on create()


class UserSerializer(UserDetailsSerializer):
    profile_type = serializers.CharField(source="userprofile.profile_type")
    birth_date = serializers.CharField(source="userprofile.birth_date")
    email_verified = serializers.BooleanField(source="userprofile.email_verified")

    class Meta(UserDetailsSerializer.Meta):
        fields = UserDetailsSerializer.Meta.fields + \
                 ('profile_type', 'birth_date', 'email_verified')

    def update(self, instance, validated_data):
        profile_data = validated_data.pop('userprofile', {})

        profile_type = profile_data.get('profile_type')
        birth_date = profile_data.get('birth_date')
        email_verified = profile_data.get('email_verified')

        instance = super(UserSerializer, self).update(instance, validated_data)

        profile = instance.userprofile
        if profile_data:
            if profile_type:
                profile.profile_type = profile_type
            if birth_date:
                profile.birth_date = birth_date
            if email_verified:
                profile.email_verified = email_verified

            profile.save()

        return instance


class MarkerSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Marker
        fields = ('url', 'title', 'description', 'full_description', 'latitude',
                  'longitude', 'zone_radius')